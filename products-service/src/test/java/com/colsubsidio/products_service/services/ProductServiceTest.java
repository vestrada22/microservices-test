package com.colsubsidio.products_service.services;

import com.colsubsidio.products_service.exceptions.ProductNotFoundException;
import com.colsubsidio.products_service.model.dto.ProductRequest;
import com.colsubsidio.products_service.model.dto.ProductResponse;
import com.colsubsidio.products_service.model.entities.Product;
import com.colsubsidio.products_service.repositories.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        productRequest = ProductRequest.builder()
                .code("0002")
                .name("Calculator")
                .description("Best calculator ever created")
                .price(20.0)
                .image("https://off.com.ph/-/media/images/off/ph/products-en/update-983/plp/overtime-group-plp.png")
                .status(true)
                .build();

        product = Product.builder()
                .id(1L)
                .code("0002")
                .name("Calculator")
                .description("Best calculator ever created")
                .price(20.0)
                .image("https://off.com.ph/-/media/images/off/ph/products-en/update-983/plp/overtime-group-plp.png")
                .status(true)
                .build();
    }

    @Test
    void addProduct() {
        productService.addProduct(productRequest);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getAllProducts() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));
        List<ProductResponse> productResponses = productService.getAllProducts();

        assertEquals(1, productResponses.size());
        assertEquals(product.getId(), productResponses.get(0).getId());
        assertEquals(product.getCode(), productResponses.get(0).getCode());
        assertEquals(product.getName(), productResponses.get(0).getName());
        assertEquals(product.getDescription(), productResponses.get(0).getDescription());
        assertEquals(product.getPrice(), productResponses.get(0).getPrice());
        assertEquals(product.getImage(), productResponses.get(0).getImage());
        assertEquals(product.getStatus(), productResponses.get(0).getStatus());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void updateProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.updateProduct(1L, productRequest);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));

        assertEquals(productRequest.getCode(), product.getCode());
        assertEquals(productRequest.getName(), product.getName());
        assertEquals(productRequest.getDescription(), product.getDescription());
        assertEquals(productRequest.getPrice(), product.getPrice());
        assertEquals(productRequest.getImage(), product.getImage());
        assertEquals(productRequest.getStatus(), product.getStatus());
    }

    @Test
    void updateProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, productRequest));

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void deleteProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, never()).delete(any(Product.class));
    }
}