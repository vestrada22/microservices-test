package com.colsubsidio.products_service.controllers;

import com.colsubsidio.products_service.model.dto.ProductRequest;
import com.colsubsidio.products_service.model.dto.ProductResponse;
import com.colsubsidio.products_service.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductRequest productRequest;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        productRequest = ProductRequest.builder()
                .code("00004")
                .name("Xbox")
                .description("Microsoft console")
                .price(20.0)
                .image("https://off.com.ph/-/media/images/off/ph/products-en/update-983/plp/overtime-group-plp.png")
                .status(true)
                .build();

        productResponse = ProductResponse.builder()
                .id(1L)
                .code("00004")
                .name("Xbox")
                .description("Microsoft console")
                .price(20.0)
                .image("https://off.com.ph/-/media/images/off/ph/products-en/update-983/plp/overtime-group-plp.png")
                .status(true)
                .build();
    }

    @Test
    void addProduct() throws Exception {
        doNothing().when(productService).addProduct(any(ProductRequest.class));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllProducts() throws Exception {
        List<ProductResponse> productList = Collections.singletonList(productResponse);
        when(productService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(productResponse.getId()))
                .andExpect(jsonPath("$[0].code").value(productResponse.getCode()))
                .andExpect(jsonPath("$[0].name").value(productResponse.getName()))
                .andExpect(jsonPath("$[0].description").value(productResponse.getDescription()))
                .andExpect(jsonPath("$[0].price").value(productResponse.getPrice()))
                .andExpect(jsonPath("$[0].image").value(productResponse.getImage()))
                .andExpect(jsonPath("$[0].status").value(productResponse.getStatus()));
    }

    @Test
    void updateProduct() throws Exception {
        doNothing().when(productService).updateProduct(any(Long.class), any(ProductRequest.class));

        mockMvc.perform(put("/api/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(any(Long.class));

        mockMvc.perform(delete("/api/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}