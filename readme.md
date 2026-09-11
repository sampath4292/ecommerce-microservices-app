# 🛒 Event-Driven E-Commerce Microservices

## 📌 Overview

The system is built using an **event-driven microservices architecture** where each business capability is isolated into its own service and maintains its own database.

Instead of tightly coupling services through synchronous REST calls, the system uses:

- **Apache Kafka** for business events and distributed communication
- **ActiveMQ** for reliable point-to-point notification tasks
- **PostgreSQL** for service-specific data persistence
- **Saga Pattern** for handling distributed transactions and compensating actions // will be implemented
- **Docker Compose** for local infrastructure

### High-Level Flow

```text
                         ┌─────────────────────┐
                         │   React Frontend    │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │    API Gateway      │
                         │      :8080          │
                         └──────────┬──────────┘
                                    │
                    ┌───────────────┼───────────────┐
                    │               │               │
                    ▼               ▼               ▼
             ┌────────────┐  ┌────────────┐  ┌────────────┐
             │   Order    │  │ Inventory  │  │  Payment   │
             │  :8081     │  │  :8082     │  │  :8083     │
             └─────┬──────┘  └─────▲──────┘  └─────┬──────┘
                   │                │               │
                   │                │               │
                   └─────── Kafka ──┴───────────────┘
                                    │
                                    ▼
                            ┌──────────────┐
                            │ Notification │
                            │    :8084     │
                            └──────┬───────┘
                                   │
                              ActiveMQ
                                   │
                                   ▼
                              Email Task


🔄 Order Processing Flow

A typical checkout follows this flow:

            Customer
               │
               ▼
            API Gateway
               │
               ▼
            Order Service
               │
               │ Create Order
               │
               ▼
            PostgreSQL
               │
               │ Publish OrderCreatedEvent
               ▼
            Apache Kafka
               │
   ├──────────────────────┐
   │                      │
   ▼                      ▼
Inventory Service     Payment Service
   │                      │
   │ Reserve Stock        │ Process Payment
   │                      │
   └──────────┬───────────┘
              │
              ▼
        Payment Result
              │
        ┌─────┴─────┐
        │           │
      SUCCESS      FAILURE
        │           │
        ▼           ▼
 Notification   Inventory
    Task         Rollback
        │           │
        ▼           ▼
    ActiveMQ     Kafka Event
        │
        ▼
 Notification Service
        │
        ▼
      Email



🏗️ Architecture

The application is divided into independent microservices.

Service	Port	Responsibility
API Gateway	8080	Single entry point and request routing
Order Service	8081	Creates and manages customer orders
Inventory Service	8082	Manages product stock and reservations
Payment Service	8083	Processes and validates payments
Notification Service	8084	Processes notification tasks

Each service follows the principle of database-per-service.

Order Service       → PostgreSQL
Inventory Service   → PostgreSQL
Payment Service     → PostgreSQL
Notification Service → Message Queue

Services do not directly access another service's database.