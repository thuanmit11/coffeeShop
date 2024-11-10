package org.example.coffeshop.services.impl;

import org.example.coffeshop.dto.GetOrdersResponse;
import org.example.coffeshop.entities.*;
import org.example.coffeshop.repositories.MenuRepository;
import org.example.coffeshop.repositories.OrderRepository;
import org.example.coffeshop.repositories.UserRepository;
import org.example.coffeshop.services.OrderService;
import org.example.coffeshop.services.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final QueueService queueService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            MenuRepository menuRepository,
                            UserRepository userRepository,
                            QueueService queueService) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
        this.queueService = queueService;
    }

    @Override
    public Order placeOrder(String username, Long menuId) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return null;
        }
        Order order = orderRepository.findByUserId(user.getId());
        if (!ObjectUtils.isEmpty(order)) {
            return null;
        }
        Menu menu = menuRepository.findById(menuId).orElse(null);
        if (menu == null) {
            return null;
        }
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setMenu(menu);
        newOrder.setStatus(Order.ORDER_STATUS_PENDING);
        Shop currentShop = menu.getShop();
        List<Queue> queues = queueService.findByShopId(currentShop.getId());
        if (queues.size() <= currentShop.getNumberOfQueues()) {
            Optional<Queue> optionalQueue = queues.stream()
                    .filter(q -> q.getCurrentSize() < currentShop.getMaxQueueSize())
                    .findFirst();
            if (optionalQueue.isPresent()) {
                Queue queue = optionalQueue.get();
                if (queues.size() == currentShop.getNumberOfQueues()
                        && queue.getCurrentSize() == currentShop.getMaxQueueSize()) {
                    return null;
                }
                queue.addCustomer(user);
                queueService.updateQueue(queue.getId(), queue);
                newOrder.setQueue(queue);

                if (queues.size() < currentShop.getNumberOfQueues()
                        && queue.getCurrentSize() == currentShop.getMaxQueueSize()) {
                    newOrder.setQueue(queueService.addQueueToShop(currentShop));
                }
            } else {
                if (queues.size() < currentShop.getNumberOfQueues()) {
                    newOrder.setQueue(queueService.addQueueToShop(currentShop));
                    orderRepository.save(newOrder);
                    return order;
                }
                orderRepository.save(newOrder);
                return null;
            }
            orderRepository.save(newOrder);
            return newOrder;
        }
        return null;
    }

    @Override
    public String serveOrder(long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (ObjectUtils.isEmpty(order)) {
            return "Order Not Found";
        }
        if (!order.getStatus().equals(Order.ORDER_STATUS_IN_QUEUE)) {
            return "Order hasn't been in queue yet";
        }
        order.setStatus(Order.ORDER_STATUS_COMPLETED);
        orderRepository.save(order);

        User user = order.getUser();
        user.setScore(user.getScore() + 1);
        userRepository.save(user);

        // Update queue
        Shop curerntShop = order.getMenu().getShop();
        List<Queue> queues = queueService.findByShopId(curerntShop.getId());
        Optional<Queue> optionalQueue = queues.stream()
                .filter(queue -> queue.getCurrentSize() == curerntShop.getMaxQueueSize()).findFirst();
        if (optionalQueue.isPresent()) {
            Queue queue = optionalQueue.get();
            queue.setCurrentSize(queue.getCurrentSize() - 1);
            queue.serveCustomer(order.getUser());
            queueService.updateQueue(queue.getId(), queue);
        }
        Queue queue = queues.get(0);
        queue.setCurrentSize(queue.getCurrentSize() - 1);
        queue.serveCustomer(order.getUser());
        queueService.updateQueue(queue.getId(), queue);


        return "Order has been completed";
    }

    @Override
    public String putInQueue(long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (ObjectUtils.isEmpty(order)) {
            return "Order not found";
        }
        Shop currentShop = order.getMenu().getShop();
        List<Order> orderOfShopInQueue =
                orderRepository.findByShopIdAndStatus(currentShop.getId(), Order.ORDER_STATUS_IN_QUEUE);
        if (orderOfShopInQueue.size() > currentShop.getNumberOfQueues() * currentShop.getMaxQueueSize()) {
            return "Queue size exceeded";
        }
        order.setStatus(Order.ORDER_STATUS_IN_QUEUE);
        orderRepository.save(order);
        return "Put in queue successfully";
    }

    public Optional<Order> getOrderStatus(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public String cancelOrder(String username, Long orderId) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (ObjectUtils.isEmpty(user)) {
            return "User not found";
        }
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (!order.getUser().equals(user)) {
                return "Order doesn't belong to current user";
            }
            order.setStatus(Order.ORDER_STATUS_CANCELED);
            orderRepository.save(order);
            return "Order cancelled";
        }
        return "Order not found";
    }

    @Override
    public List<GetOrdersResponse> getAllByUserName(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (ObjectUtils.isEmpty(user)) {
            return null;
        }
        if (Role.OPERATOR.equals(user.getRole().getName())) {
            List<Order> orders = orderRepository.findAll();
            return orders.stream().map(order -> {
                GetOrdersResponse response = new GetOrdersResponse();
                response.setItemName(order.getMenu().getItemName());
                response.setPrice(order.getMenu().getPrice());
                response.setStatus(order.getStatus());
                return response;
            }).toList();
        }
        Order order = orderRepository.findByUserId(user.getId());
        if (ObjectUtils.isEmpty(order)) {
            return List.of();
        }
        GetOrdersResponse response = new GetOrdersResponse();
        response.setItemName(order.getMenu().getItemName());
        response.setCustomerName(order.getUser().getUsername());
        response.setPrice(order.getMenu().getPrice());
        response.setStatus(order.getStatus());
        return List.of(response);
    }
}

