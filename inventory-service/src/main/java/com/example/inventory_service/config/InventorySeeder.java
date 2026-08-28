package com.example.inventory_service.config;

import com.example.inventory_service.entity.Product;
import com.example.inventory_service.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class InventorySeeder implements CommandLineRunner {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public void run(String... args) throws Exception {
        if (inventoryRepository.count() == 0) {
            Product prod1 = new Product("PROD-001", "Wireless Mouse", 100, new BigDecimal("49.99"));
            Product prod2 = new Product("PROD-002", "Mechanical Keyboard", 50, new BigDecimal("19.99"));
            
            inventoryRepository.save(prod1);
            inventoryRepository.save(prod2);
            System.out.println("Mock Inventory Loaded!");
        }
    }
}