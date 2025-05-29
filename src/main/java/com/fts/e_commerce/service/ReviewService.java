package com.fts.e_commerce.service;

import com.fts.e_commerce.entity.ProductEntity;
import com.fts.e_commerce.entity.ReviewEntity;
import com.fts.e_commerce.entity.UserEntity;
import com.fts.e_commerce.repository.ProductRepository;
import com.fts.e_commerce.repository.ReviewRepository;
import com.fts.e_commerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewEntity addReview(Long productId, Long userId, ReviewEntity review) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        review.setProduct(product);
        review.setUser(user);
        ReviewEntity savedReview = reviewRepository.save(review);

        // Update product average rating
        List<ReviewEntity> reviews = getProductReviews(productId);
        double averageRating = reviews.stream()
                .mapToInt(ReviewEntity::getRating)
                .average()
                .orElse(0.0);
        product.setAverageRating(averageRating);
        productRepository.save(product);

        return savedReview;
    }

    public List<ReviewEntity> getProductReviews(Long productId) {
        return reviewRepository.findByProductId(productId);
    }
}