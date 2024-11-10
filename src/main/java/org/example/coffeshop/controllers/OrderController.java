package org.example.coffeshop.controllers;

import org.example.coffeshop.dto.GetOrdersResponse;
import org.example.coffeshop.entities.Order;
import org.example.coffeshop.services.JWTService;
import org.example.coffeshop.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;
    private final JWTService jwtService;

    @Autowired
    public OrderController(OrderService orderService, JWTService jwtService) {
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(@RequestHeader("Authorization") String authorizationHeader,
                                            @RequestParam(name="menu_id") Long menuId) {
        String username = jwtService.extractUserName(authorizationHeader.substring(7));
        Order order = orderService.placeOrder(username, menuId);
        if (ObjectUtils.isEmpty(order)) {
            return new ResponseEntity<>("Queue size exceeded or user already ordered", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Order success", HttpStatus.CREATED);
    }

    @PutMapping("{orderId}/serve")
    public ResponseEntity<String> serveOrder(@PathVariable long orderId) {
        return new ResponseEntity<>(orderService.serveOrder(orderId), HttpStatus.OK);
    }

    @GetMapping("{shopId}")
    public ResponseEntity<List<GetOrdersResponse>> getOrders(@RequestHeader("Authorization") String authorizationHeader,
                                                             @PathVariable Long shopId) {
        String username = jwtService.extractUserName(authorizationHeader.substring(7));
        return new ResponseEntity<>(orderService.getAllByUserNameAndShopId(username, shopId), HttpStatus.OK);
    }

    @PutMapping("{orderId}/queue")
    public ResponseEntity<String> putInQueue(@PathVariable long orderId) {
        return new ResponseEntity<>(orderService.putInQueue(orderId), HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@RequestHeader("Authorization") String authorizationHeader,
                                            @PathVariable Long orderId) {
        String username = jwtService.extractUserName(authorizationHeader.substring(7));
        return new ResponseEntity<>(orderService.cancelOrder(username, orderId), HttpStatus.OK);
    }
}
