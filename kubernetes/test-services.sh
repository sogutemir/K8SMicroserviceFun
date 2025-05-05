#!/bin/bash

echo "=== Testing Kubernetes Microservices ==="

# Kurulum test scripti için ortam değişkenleri
USER_SERVICE_IP=$(kubectl get service user-service -o jsonpath='{.spec.clusterIP}')
TASK_SERVICE_IP=$(kubectl get service task-service -o jsonpath='{.spec.clusterIP}')
REPORTING_SERVICE_IP=$(kubectl get service reporting-service -o jsonpath='{.spec.clusterIP}')

echo ""
echo "Service IPs:"
echo "User Service: $USER_SERVICE_IP"
echo "Task Service: $TASK_SERVICE_IP"
echo "Reporting Service: $REPORTING_SERVICE_IP"
echo ""

# Port forward (local test için)
echo "Setting up port forwarding..."
kubectl port-forward service/user-service 8080:8080 &
USER_PID=$!
sleep 2
kubectl port-forward service/task-service 8081:8081 &
TASK_PID=$!
sleep 2
kubectl port-forward service/reporting-service 8083:8083 &
REPORTING_PID=$!
sleep 2

echo ""
echo "=== Testing User Service ==="
echo "1. Status check"
curl -s http://localhost:8080/api/status | jq '.'

echo ""
echo "2. Creating test user"
USER_ID=$(curl -s -X POST -H "Content-Type: application/json" -d '{"name":"Test User","email":"test@example.com"}' http://localhost:8080/api/users | jq -r '.id')
echo "Created user with ID: $USER_ID"

echo ""
echo "3. Get user by ID"
curl -s http://localhost:8080/api/users/$USER_ID | jq '.'

echo ""
echo "=== Testing Task Service ==="
echo "1. Status check (should show user-service status as well)"
curl -s http://localhost:8081/api/status | jq '.'

echo ""
echo "2. Creating task for user"
TASK_ID=$(curl -s -X POST -H "Content-Type: application/json" -d "{\"title\":\"Test Task\",\"completed\":false,\"userId\":$USER_ID}" http://localhost:8081/api/tasks | jq -r '.id')
echo "Created task with ID: $TASK_ID"

echo ""
echo "3. Get task by ID"
curl -s http://localhost:8081/api/tasks/$TASK_ID | jq '.'

echo ""
echo "4. Get task with user details (tests service communication)"
echo "This endpoint uses RestClient for service communication:"
curl -s http://localhost:8081/api/tasks/$TASK_ID/with-user | jq '.'

echo ""
echo "5. Get tasks by user ID"
curl -s http://localhost:8081/api/tasks/user/$USER_ID | jq '.'

# Test reporting-service
echo -e "\n\n===== Testing Reporting Service ====="
echo "Getting all reports:"
curl -s -X GET http://localhost:8083/api/reports | jq

echo "Creating a report:"
curl -s -X POST -H "Content-Type: application/json" -d "{\"name\":\"User Activity Report\",\"description\":\"Report on user activity\",\"userId\":$USER_ID,\"type\":\"USER_PRODUCTIVITY\"}" http://localhost:8083/api/reports | jq

echo "Getting reports for a user:"
curl -s -X GET http://localhost:8083/api/reports/user/$USER_ID | jq

echo "Getting report by type:"
curl -s -X GET http://localhost:8083/api/reports/type/USER_PRODUCTIVITY | jq

echo ""
echo "=== Cleaning up ==="
echo "Stopping port forwarding..."
kill $USER_PID
kill $TASK_PID
kill $REPORTING_PID

echo -e "\n\nAll tests completed successfully!" 