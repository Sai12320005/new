package com.fts.e_commerce.controller;

import com.fts.e_commerce.entity.ProductEntity;
import com.fts.e_commerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<ProductEntity> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/category/{category}")
    public List<ProductEntity> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/search")
    public List<ProductEntity> searchProducts(@RequestParam String query) {
        return productService.searchProducts(query);
    }

    @PostMapping
    public ResponseEntity<ProductEntity> addProduct(@RequestBody ProductEntity product) {
        return ResponseEntity.ok(productService.saveProduct(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}