@echo off
REM Build Docker image for Task Management API

SET VERSION=%1
IF "%VERSION%"=="" SET VERSION=latest

echo Building Docker image: task-management-api:%VERSION%...

docker build -t task-management-api:%VERSION% --build-arg VERSION=%VERSION% .
