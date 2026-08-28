package com.example.payment_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderCreatedEvent {
    private Long orderId;
    private String userId;
    private BigDecimal totalAmount;
}