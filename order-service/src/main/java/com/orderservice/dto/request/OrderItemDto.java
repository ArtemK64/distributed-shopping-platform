package com.orderservice.dto.request;

import lombok.Data;

@Data
public class OrderItemDto {

    private String productName;
    private Integer quantity;
}
