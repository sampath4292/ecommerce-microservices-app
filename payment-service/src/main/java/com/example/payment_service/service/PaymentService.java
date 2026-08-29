package com.example.payment_service.service;

import com.example.payment_service.dto.NotificationTask;
import com.example.payment_service.dto.OrderCreatedEvent;
import com.example.payment_service.entity.Payment;
import com.example.payment_service.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JmsTemplate jmsTemplate; // Spring's tool for sending ActiveMQ messages

    @KafkaListener(topics = "order-created", groupId = "payment-group")
    public void processPayment(String eventMessage) {
        try {
            // 1. Parse the Kafka Event
            OrderCreatedEvent event = objectMapper.readValue(eventMessage, OrderCreatedEvent.class);// Deserialize JSON to OrderCreatedEvent
            System.out.println("------------- PAYMENT INITIATED -------------");
            System.out.println("Processing payment for Order ID: " + event.getOrderId());

            // 2. Simulate Payment Gateway Delay
            Thread.sleep(2000); 

            // 3. Save Transaction to PostgreSQL
            Payment payment = new Payment();
            payment.setOrderId(event.getOrderId());
            payment.setUserId(event.getUserId());
            payment.setAmount(event.getTotalAmount());
            payment.setStatus("SUCCESS");
            paymentRepository.save(payment);
            System.out.println("Payment SUCCESS for amount: $" + event.getTotalAmount());

            // 4. The Handoff: Send Task to ActiveMQ
            NotificationTask task = new NotificationTask(
                    event.getOrderId(), 
                    event.getUserId(), 
                    "Your payment of $" + event.getTotalAmount() + " was successful!"
            );
            
            // Serialize to JSON and drop it in the "email-queue"
            String taskJson = objectMapper.writeValueAsString(task);
            jmsTemplate.convertAndSend("email-queue", taskJson);
            
            System.out.println("Receipt task securely queued in ActiveMQ!");
            System.out.println("---------------------------------------------");

        } catch (Exception e) {
            System.err.println("Payment processing failed: " + e.getMessage());
        }
    }
}