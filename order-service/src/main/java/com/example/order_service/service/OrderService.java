package com.example.order_service.service;

import com.example.order_service.dto.OrderCreatedEvent;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "order-created";

    public String placeOrder(OrderRequest orderRequest) {
        // 1. Create the Master Order
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setStatus("PENDING");

        // 2. Map DTO items to Entity items and calculate total amount
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = orderRequest.getItems().stream().map(itemDto -> {
            OrderItem item = new OrderItem();
            item.setProductId(itemDto.getProductId());
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(itemDto.getPrice());
            item.setOrder(order); // Link child to parent
            return item;
        }).collect(Collectors.toList());

        for (OrderItem item : orderItems) {
            totalAmount = totalAmount.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        // 3. Save to PostgreSQL (CascadeType.ALL saves the items automatically!)
        Order savedOrder = orderRepository.save(order);

        // 4. Publish Event to Kafka
        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getTotalAmount(),
                orderRequest.getItems()
        );
        kafkaTemplate.send(TOPIC, event);

        return "Order Placed Successfully. Order ID: " + savedOrder.getId();
    }
}