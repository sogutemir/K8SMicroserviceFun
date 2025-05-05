@echo off
echo Starting Kubernetes microservice application...

cd %~dp0..

REM PostgreSQL resources apply
echo Applying PostgreSQL resources...
kubectl apply -f kubernetes/postgres/postgres-secret.yaml
kubectl apply -f kubernetes/postgres/postgres-pvc.yaml
kubectl apply -f kubernetes/postgres/postgres-init-configmap.yaml
kubectl apply -f kubernetes/postgres/postgres-deployment.yaml
kubectl apply -f kubernetes/postgres/postgres-service.yaml

REM Wait for PostgreSQL to be ready
echo Waiting for PostgreSQL to be ready...
kubectl wait --for=condition=ready pod -l app=postgres --timeout=120s

REM Reporting DB'yi oluştur
echo Reporting DB database is being created...
kubectl exec -it deployment/postgres -- psql -U postgres -c "CREATE DATABASE reportingdb WITH OWNER postgres ENCODING 'UTF8';" || echo ReportingDB database might already exist, continuing...

REM User-service resources apply
echo Applying User Service resources...
kubectl apply -f kubernetes/user-service/configmap.yaml
kubectl apply -f kubernetes/user-service/deployment.yaml
kubectl apply -f kubernetes/user-service/service.yaml

REM Task-service resources apply
echo Applying Task Service resources...
kubectl apply -f kubernetes/task-service/configmap.yaml
kubectl apply -f kubernetes/task-service/service.yaml
kubectl apply -f kubernetes/task-service/deployment.yaml

REM Reporting-service resources apply
echo Applying Reporting Service resources...
kubectl apply -f kubernetes/reporting-service/configmap.yaml
kubectl apply -f kubernetes/reporting-service/service.yaml
kubectl apply -f kubernetes/reporting-service/deployment.yaml

echo All services started. Waiting for pods to be ready...
echo Port-forward processes will be started with check-and-forward.bat...

REM Wait for pods to be ready and start port-forward
call %~dp0check-and-forward.bat 