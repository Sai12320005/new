package com.fts.e_commerce.service;

import com.fts.e_commerce.entity.*;
import com.fts.e_commerce.repository.OrderRepository;
import com.fts.e_commerce.repository.ProductRepository;
import com.fts.e_commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderEntity createOrder(Long userId, CartEntity cart, String deliveryAddress, String paymentMethod) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setTotalAmount(cart.getTotalAmount());
        order.setDeliveryAddress(deliveryAddress);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus("PENDING");
        order.setOrderStatus("PROCESSING");
        order.setOrderDate(LocalDateTime.now());
        order.setTrackingNumber(generateTrackingNumber());

        // Convert cart items to order items
        cart.getItems().forEach(cartItem -> {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            order.getItems().add(orderItem);

            // Update product stock
            ProductEntity product = cartItem.getProduct();
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        });

        return orderRepository.save(order);
    }

    public List<OrderEntity> getUserOrders(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    private String generateTrackingNumber() {
        return "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}