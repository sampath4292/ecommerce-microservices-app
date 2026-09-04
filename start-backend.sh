#!/bin/bash

PROJECT_DIR="$(pwd)"

echo "======================================"
echo " Starting Docker infrastructure"
echo "======================================"

docker compose up -d

echo ""
echo "======================================"
echo " Opening microservice terminals"
echo "======================================"

env -u GTK_PATH -u GIO_MODULE_DIR \
gnome-terminal --title="API Gateway" -- bash -c "
    cd '$PROJECT_DIR/api-gateway'
    echo '========== API GATEWAY =========='
    ./mvnw spring-boot:run
    exec bash
"

env -u GTK_PATH -u GIO_MODULE_DIR \
gnome-terminal --title="Order Service" -- bash -c "
    cd '$PROJECT_DIR/order-service'
    echo '========== ORDER SERVICE =========='
    ./mvnw spring-boot:run
    exec bash
"

env -u GTK_PATH -u GIO_MODULE_DIR \
gnome-terminal --title="Inventory Service" -- bash -c "
    cd '$PROJECT_DIR/inventory-service'
    echo '========== INVENTORY SERVICE =========='
    ./mvnw spring-boot:run
    exec bash
"

env -u GTK_PATH -u GIO_MODULE_DIR \
gnome-terminal --title="Payment Service" -- bash -c "
    cd '$PROJECT_DIR/payment-service'
    echo '========== PAYMENT SERVICE =========='
    ./mvnw spring-boot:run
    exec bash
"

env -u GTK_PATH -u GIO_MODULE_DIR \
gnome-terminal --title="Notification Service" -- bash -c "
    cd '$PROJECT_DIR/notification-service'
    echo '========== NOTIFICATION SERVICE =========='
    ./mvnw spring-boot:run
    exec bash
"

echo ""
echo "All backend terminals opened."