package com.fts.e_commerce.service;

import com.fts.e_commerce.entity.ProductEntity;
import com.fts.e_commerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public List<ProductEntity> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<ProductEntity> searchProducts(String query) {
        return productRepository.search(query);
    }

    public ProductEntity saveProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}