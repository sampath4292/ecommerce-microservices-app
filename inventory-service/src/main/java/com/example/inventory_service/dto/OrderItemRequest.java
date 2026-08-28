package com.example.inventory_service.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemRequest {
    private String productId;
    private Integer quantity;
    private BigDecimal price;
}