package com.example.inventory_service.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCreatedEvent {
    private Long orderId;
    private String userId;
    private BigDecimal totalAmount;
    private List<OrderItemRequest> items;
}