package com.example.notification_service.service;

import com.example.notification_service.dto.NotificationTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private ObjectMapper objectMapper;

    // Listen directly to the ActiveMQ queue
    @JmsListener(destination = "email-queue")
    public void processEmailTask(String taskJson) {
        try {
            // 1. Parse the JSON back into our Java Object
            NotificationTask task = objectMapper.readValue(taskJson, NotificationTask.class);

            System.out.println("------------- EMAIL DISPATCH -------------");
            System.out.println("Connecting to SMTP Server...");
            
            // Simulate network delay for sending an email
            Thread.sleep(1500); 

            System.out.println("To: " + task.getUserId() + "@example.com");
            System.out.println("Subject: Receipt for Order #" + task.getOrderId());
            System.out.println("Body: " + task.getMessage());
            System.out.println("Status: DELIVERED SUCCESSFULLY");
            System.out.println("------------------------------------------");

        } catch (Exception e) {
            System.err.println("Failed to process email task: " + e.getMessage());
        }
    }
}