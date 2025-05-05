@echo off
echo Starting port-forward process for Kubernetes services...

REM PostgreSQL port-forward
start cmd /k kubectl port-forward svc/postgres 5432:5432

REM User Service port-forward
start cmd /k kubectl port-forward svc/user-service 8080:8080

REM Task Service port-forward
start cmd /k kubectl port-forward svc/task-service 8081:8081

REM Reporting Service port-forward
start cmd /k kubectl port-forward svc/reporting-service 8083:8083

echo Port-forward processes started.
echo User-Service: http://localhost:8080
echo Task-Service: http://localhost:8081
echo Reporting-Service: http://localhost:8083
echo PostgreSQL: localhost:5432

echo To stop the port-forward process, you can close the command windows.
pause 