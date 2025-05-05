@echo off
echo Cleaning up Kubernetes microservice application...

REM Reporting Service resources cleanup
echo Cleaning up Reporting Service resources...
kubectl delete -f kubernetes/reporting-service/deployment.yaml --ignore-not-found
kubectl delete -f kubernetes/reporting-service/service.yaml --ignore-not-found
kubectl delete -f kubernetes/reporting-service/configmap.yaml --ignore-not-found

REM Task Service resources cleanup
echo Cleaning up Task Service resources...
kubectl delete -f kubernetes/task-service/deployment.yaml --ignore-not-found
kubectl delete -f kubernetes/task-service/service.yaml --ignore-not-found
kubectl delete -f kubernetes/task-service/configmap.yaml --ignore-not-found

REM User Service resources cleanup
echo Cleaning up User Service resources...
kubectl delete -f kubernetes/user-service/deployment.yaml --ignore-not-found
kubectl delete -f kubernetes/user-service/service.yaml --ignore-not-found
kubectl delete -f kubernetes/user-service/configmap.yaml --ignore-not-found

REM PostgreSQL resources cleanup
echo Cleaning up PostgreSQL resources...
kubectl delete -f kubernetes/postgres/postgres-service.yaml --ignore-not-found
kubectl delete -f kubernetes/postgres/postgres-deployment.yaml --ignore-not-found
kubectl delete -f kubernetes/postgres/postgres-init-configmap.yaml --ignore-not-found
kubectl delete -f kubernetes/postgres/postgres-pvc.yaml --ignore-not-found
kubectl delete -f kubernetes/postgres/postgres-secret.yaml --ignore-not-found

echo All services cleaned up.
pause 