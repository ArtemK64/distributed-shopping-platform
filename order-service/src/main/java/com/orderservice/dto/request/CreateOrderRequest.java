package com.orderservice.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    private CustomerDto customer;
    private ShippingAddressDto shippingAddress;
    private List<OrderItemDto> items;
}
