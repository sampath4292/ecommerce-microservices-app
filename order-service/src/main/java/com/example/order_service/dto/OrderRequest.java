//What the frontend sends you
package com.example.order_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private String userId;
    private List<OrderItemRequest> items;
}