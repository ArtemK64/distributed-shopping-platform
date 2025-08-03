package com.inventoryservice.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResultEvent {

    private Long orderId;
    private boolean confirmed;
    private String message;
    private BigDecimal totalPrice;
    private Map<String, BigDecimal> itemPrices;
    private String customerEmail;
}
