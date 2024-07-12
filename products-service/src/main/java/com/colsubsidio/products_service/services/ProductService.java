package com.colsubsidio.products_service.services;

import com.colsubsidio.products_service.exceptions.ProductNotFoundException;
import com.colsubsidio.products_service.model.dto.ProductRequest;
import com.colsubsidio.products_service.model.dto.ProductResponse;
import com.colsubsidio.products_service.model.entities.Product;
import com.colsubsidio.products_service.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void addProduct(ProductRequest product) {
        var newProduct = Product.builder()
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .image(product.getImage())
                .status(product.getStatus())
                .build();

        productRepository.save(newProduct);

        log.info("New product added: {}", newProduct);
    }

    public List<ProductResponse> getAllProducts() {
        var products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    @Transactional
    public void updateProduct(Long id, ProductRequest request) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));

        validateProductRequest(request);

        product.setCode(request.getCode());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setImage(request.getImage());
        product.setStatus(request.getStatus());

        productRepository.save(product);

        log.info("Product with ID {} updated: {}", id, product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));

        productRepository.delete(product);

        log.info("Product with ID {} deleted", id);
    }

    private void validateProductRequest(ProductRequest request) {
        if (request.getCode() == null || request.getCode().isEmpty()) {
            throw new IllegalArgumentException("Code cannot be null or empty");
        }
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (request.getPrice() == null) {
            throw new IllegalArgumentException("Price cannot be null or negative");
        }
        if (request.getImage() == null || request.getImage().isEmpty()) {
            throw new IllegalArgumentException("Image cannot be null or empty");
        }
        if (request.getStatus() == null) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .image(product.getImage())
                .status(product.getStatus())
                .build();
    }
}
