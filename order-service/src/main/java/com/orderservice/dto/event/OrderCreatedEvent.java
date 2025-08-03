package com.orderservice.dto.event;

import com.orderservice.dto.request.OrderItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {

    private Long orderId;
    private String customerEmail;
    private List<OrderItemDto> items;
}
