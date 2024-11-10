package org.example.coffeshop.services.impl;


import org.example.coffeshop.dto.GetQueueResponse;
import org.example.coffeshop.dto.GetShopsResponse;
import org.example.coffeshop.dto.UpdateShopRequest;
import org.example.coffeshop.entities.Order;
import org.example.coffeshop.entities.Queue;
import org.example.coffeshop.entities.Shop;
import org.example.coffeshop.entities.User;
import org.example.coffeshop.repositories.OrderRepository;
import org.example.coffeshop.repositories.ShopRepository;
import org.example.coffeshop.repositories.UserRepository;
import org.example.coffeshop.services.GeoCodingService;
import org.example.coffeshop.services.QueueService;
import org.example.coffeshop.services.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.filter.OrderedFormContentFilter;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GeoCodingService geoCodingService;
    private final QueueService queueService;
    private final OrderedFormContentFilter formContentFilter;

    @Autowired
    public ShopServiceImpl(ShopRepository shopRepository, OrderRepository orderRepository,
                           UserRepository userRepository,
                           GeoCodingService geoCodingService,
                           QueueService queueService, OrderedFormContentFilter formContentFilter) {
        this.shopRepository = shopRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.geoCodingService = geoCodingService;
        this.queueService = queueService;
        this.formContentFilter = formContentFilter;
    }

    public List<GetShopsResponse> getAllShops() {
        List<Shop> result = shopRepository.findAll();
        return result.stream().map(GetShopsResponse::new).toList();
    }

    public GetShopsResponse getShopById(Long id) {
        Optional<Shop> shop = shopRepository.findById(id);
        return shop.map(GetShopsResponse::new).orElse(null);
    }

    public String createShop(Shop shop) {
        extractCoordinates(shop);
        if (shop.getNumberOfQueues() > 3) {
            return "Shop can only have at most 3 queues";
        }
        shopRepository.save(shop);
        queueService.addQueueToShop(shop);
        return "Created shop successfully";
    }

    private void extractCoordinates(Shop shop) {
        Map<String, Double> coordinates = geoCodingService.getCoordinates(shop.getLocation());

        if (coordinates.containsKey("latitude") && coordinates.containsKey("longitude")) {
            shop.setLatitude(coordinates.get("latitude"));
            shop.setLongitude(coordinates.get("longitude"));
        }
    }

    public String updateShop(Long id, UpdateShopRequest shopDetails) {
        shopRepository.findById(id).map(shop -> {
            if (shopDetails.getMaxQueueSize() > shop.getNumberOfQueues()
                    || shopDetails.getNumberOfQueues() > shop.getNumberOfQueues()) {
                updateQueuesAndOrders(shopDetails, shop);
            }
            shop.setLocation(shopDetails.getLocation());
            shop.setContactDetails(shopDetails.getContactDetails());
            shop.setMaxQueueSize(shopDetails.getMaxQueueSize());
            shop.setNumberOfQueues(shopDetails.getNumberOfQueues());
            shop.setOpeningTime(shopDetails.getOpeningTime());
            shop.setClosingTime(shopDetails.getClosingTime());
            extractCoordinates(shop);

            queueService.updateQueues(shop.getQueues(), shop);
            shopRepository.save(shop);
            return "Updated shop successfully";
        });
        return "Shop updated successfully";
    }

    private void updateQueuesAndOrders(UpdateShopRequest updateShop, Shop currentShop) {
        List<Queue> queues = currentShop.getQueues();
        int newMaxQueueSize = updateShop.getMaxQueueSize();
        int newNumberOfQueue = updateShop.getNumberOfQueues();
        int oldNumberOfQueue = currentShop.getNumberOfQueues();
        int oldMaxQueueSize = currentShop.getMaxQueueSize();
        Queue lastQueue = queues.stream().filter(q -> q.getCurrentSize() < newMaxQueueSize).findAny().orElse(null);
        List<Order> orders = orderRepository.findByShopIdAndQueueIsNull(currentShop.getId());

        // new queue size is bigger
        if (!ObjectUtils.isEmpty(lastQueue)) {
            for (int i = 0; i < newMaxQueueSize - oldMaxQueueSize; i++) { // finish the last old queue
                lastQueue.setMaxSize(newMaxQueueSize);
                if (updateOrder(orders, lastQueue)) break;
            }
        }
        // queue size doesn't change then check number of queues
        int newQueueNumber = newNumberOfQueue - oldNumberOfQueue;
        if (newQueueNumber > 0) {
            currentShop.setNumberOfQueues(newNumberOfQueue);
            currentShop.setMaxQueueSize(newMaxQueueSize);
            for (int i = 0; i < newQueueNumber; i++) {
                Queue queue = queueService.addQueueToShop(currentShop);
                for (int j = 0; j < newMaxQueueSize; j++) {
                    if (updateOrder(orders, queue)) break;
                }
            }
        }
    }

    private boolean updateOrder(List<Order> orders, Queue queue) {
        if (orders.isEmpty()) {
            return true;
        }
        Order order = orders.get(0);
        queue.addCustomer(order.getUser());
        queueService.updateQueue(queue.getId(), queue);
        order.setQueue(queue);
        userRepository.save(order.getUser());
        orders.remove(order);
        return false;
    }

    public boolean deleteShop(Long id) {
        return shopRepository.findById(id).map(shop -> {
            shopRepository.delete(shop);
            return true;
        }).orElse(false);
    }

    @Override
    public GetShopsResponse getShopNearBy(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return null;
        }
        User user = optionalUser.get();
        List<Shop> nearByShops =
                shopRepository.findShopsNearLocation(user.getLatitude(), user.getLongitude(), 100000);
        if (nearByShops.isEmpty()) {
            return null;
        }
        Shop shop = nearByShops.get(0);
        return new GetShopsResponse(shop);
    }

    @Override
    public GetQueueResponse getQueuesByShopId(Long shopId) {
        Optional<Shop> currentShop = shopRepository.findById(shopId);
        if (currentShop.isPresent()) {
            List<Queue> queues = queueService.findByShopId(shopId);
            GetQueueResponse response = new GetQueueResponse();
            response.setMaxNumberOfQueues(currentShop.get().getNumberOfQueues());
            response.setMaxQueueSize(currentShop.get().getMaxQueueSize());
            List<GetQueueResponse.QueueResponse> queueResponses = new ArrayList<>();
            queues.forEach(queue -> {
                List<Order> orders = queue.getOrders();
                List<GetQueueResponse.QueueResponse.OrderResponse> orderResponses = new ArrayList<>();

                orderResponses = orders.stream().map(order -> {
                    GetQueueResponse.QueueResponse.OrderResponse orderResponse = new GetQueueResponse.QueueResponse.OrderResponse();
                    orderResponse.setId(order.getId());
                    orderResponse.setStatus(order.getStatus());
                    orderResponse.setItemName(order.getMenu().getItemName());
                    orderResponse.setCustomerName(order.getUser().getUsername());
                    return orderResponse;
                }).toList();

                queueResponses.add(new GetQueueResponse.QueueResponse(queue.getId(), queue.getCurrentSize(), orderResponses));
            });
            response.setQueues(queueResponses);
            return response;
        }
        return null;
    }
}
