#!/bin/bash

# Maven ile user-service build
echo "Building User Service with Maven..."
cd ..
cd user-service
mvn clean package -DskipTests

# Docker imajı
echo "Building User Service Docker image..."
docker build -t user-service:latest .
cd ..

# Maven ile task-service build
echo "Building Task Service with Maven..."
cd task-service
mvn clean package -DskipTests

# Docker imajı
echo "Building Task Service Docker image..."
docker build -t task-service:latest .
cd ..

# Maven ile reporting-service build
echo "Building Reporting Service with Maven..."
cd reporting-service
mvn clean package -DskipTests

# Docker imajı
echo "Building Reporting Service Docker image..."
docker build -t reporting-service:latest .
cd ../kubernetes

# Kubernetes'e deploy
echo "Deploying to Kubernetes..."

# PostgreSQL kaynaklarını uygula
echo "Deploying PostgreSQL resources..."
kubectl apply -f postgres/postgres-secret.yaml
kubectl apply -f postgres/postgres-pvc.yaml
kubectl apply -f postgres/postgres-init-configmap.yaml
kubectl apply -f postgres/postgres-deployment.yaml
kubectl apply -f postgres/postgres-service.yaml

# PostgreSQL'in hazır olmasını bekle
echo "Waiting for PostgreSQL to be ready..."
kubectl wait --for=condition=ready pod -l app=postgres --timeout=120s

# User-service kaynaklarını uygula
echo "Deploying User Service resources..."
kubectl apply -f user-service/configmap.yaml
kubectl apply -f user-service/deployment.yaml
kubectl apply -f user-service/service.yaml

echo "Deploying task-service..."
kubectl apply -f task-service/configmap.yaml
kubectl apply -f task-service/service.yaml
kubectl apply -f task-service/deployment.yaml

echo "Deploying reporting-service..."
kubectl apply -f reporting-service/configmap.yaml
kubectl apply -f reporting-service/service.yaml
kubectl apply -f reporting-service/deployment.yaml

echo "Deployment completed successfully!"
echo "To check the status of your pods, run: kubectl get pods"
echo "To check the services, run: kubectl get services"
echo "To view logs for user-service: kubectl logs -f deployment/user-service" 