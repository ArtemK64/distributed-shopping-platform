package com.inventoryservice.controller;

import com.inventoryservice.dto.response.ProductInfoDto;
import com.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final InventoryService inventoryService;

    @GetMapping
    public List<ProductInfoDto> getAllWithStock() {
        return inventoryService.getProductInfoList();
    }
}
