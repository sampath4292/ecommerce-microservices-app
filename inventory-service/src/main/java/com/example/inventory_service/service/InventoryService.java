package com.example.inventory_service.service;

import com.example.inventory_service.dto.OrderCreatedEvent;
import com.example.inventory_service.dto.OrderItemRequest;
import com.example.inventory_service.entity.Product;
import com.example.inventory_service.repository.InventoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ObjectMapper objectMapper; // Built into Spring Boot

    // This annotation tells Spring to constantly poll this topic
    @KafkaListener(topics = "order-created", groupId = "inventory-group")
    @Transactional
    public void consumeOrderEvent(String eventMessage) {
        try {
            // 1. Convert the JSON string back into our Java Object
            OrderCreatedEvent event = objectMapper.readValue(eventMessage, OrderCreatedEvent.class);
            System.out.println("------------- NEW EVENT -------------");
            System.out.println("Received Order ID: " + event.getOrderId() + " from Kafka!");

            // 2. Loop through the items and deduct stock
            for (OrderItemRequest item : event.getItems()) {
                Optional<Product> productOpt = inventoryRepository.findById(item.getProductId());
                
                if (productOpt.isPresent()) {
                    Product product = productOpt.get();
                    int newQuantity = product.getAvailableQuantity() - item.getQuantity();
                    
                    if (newQuantity >= 0) {
                        product.setAvailableQuantity(newQuantity);
                        inventoryRepository.save(product);
                        System.out.println("Stock updated for " + product.getProductId() + ". Remaining: " + newQuantity);
                    } else {
                        System.err.println("Not enough stock for " + product.getProductId());
                    }
                } else {
                    System.err.println("Product not found: " + item.getProductId());
                }
            }
            System.out.println("-------------------------------------");

        } catch (Exception e) {
            System.err.println("Error processing Kafka message: " + e.getMessage());
        }
    }
}