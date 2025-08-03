package com.inventoryservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductInfoDto {
    private Long id;
    private String name;
    private String category;
    private String manufacturer;
    private Integer quantity;
    private BigDecimal price;
}
