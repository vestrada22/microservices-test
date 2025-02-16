package com.colsubsidio.products_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    private String code;
    private String name;
    private String description;
    private Double price;
    private String image;
    private Boolean status;
}
