package com.fts.e_commerce.controller;

import com.fts.e_commerce.entity.CartEntity;
import com.fts.e_commerce.entity.OrderEntity;
import com.fts.e_commerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<OrderEntity> createOrder(
            @PathVariable Long userId,
            @RequestBody CartEntity cart,
            @RequestParam String deliveryAddress,
            @RequestParam String paymentMethod) {
        OrderEntity order = orderService.createOrder(userId, cart, deliveryAddress, paymentMethod);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderEntity>> getUserOrders(@PathVariable Long userId) {
        List<OrderEntity> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }
}