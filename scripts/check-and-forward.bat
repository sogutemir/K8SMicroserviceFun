@echo off
echo Checking if pods are ready...

:CHECK_PODS
kubectl get pods | findstr "Running" | findstr /c:"1/1" | findstr /c:"postgres" | findstr /c:"user-service" | findstr /c:"task-service" | findstr /c:"reporting-service" > nul
if %errorlevel% neq 0 (
    echo Pods are still initializing... Will check again in 5 seconds.
    timeout /t 5 /nobreak > nul
    goto CHECK_PODS
)

echo All pods are ready! Starting port-forward process...
call %~dp0port-forward.bat