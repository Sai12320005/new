package com.fts.e_commerce.repository;

import com.fts.e_commerce.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByProductId(Long productId);
}