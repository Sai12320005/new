package com.fts.e_commerce.repository;

import com.fts.e_commerce.entity.OrderEntity;
import com.fts.e_commerce.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserOrderByOrderDateDesc(UserEntity user);
}