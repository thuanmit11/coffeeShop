package org.example.coffeshop.services;

import org.example.coffeshop.dto.GetOrdersResponse;
import org.example.coffeshop.entities.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order placeOrder(String username, Long menuId);
    String serveOrder(long orderId);
    String putInQueue(long orderId);
    Optional<Order> getOrderStatus(Long orderId);
    String cancelOrder(String username, Long orderId);
    List<GetOrdersResponse> getAllByUserName(String username);
}
