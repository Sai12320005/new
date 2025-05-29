package com.fts.e_commerce.controller;

import com.fts.e_commerce.entity.ReviewEntity;
import com.fts.e_commerce.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/product/{productId}/user/{userId}")
    public ResponseEntity<ReviewEntity> addReview(
            @PathVariable Long productId,
            @PathVariable Long userId,
            @RequestBody ReviewEntity review) {
        return ResponseEntity.ok(reviewService.addReview(productId, userId, review));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewEntity>> getProductReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId));
    }
}