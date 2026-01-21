#!/bin/bash
set -e

VERSION=${1:-latest}

echo "Building Docker image task-management-api:$VERSION..."

docker build -t task-management-api:$VERSION --build-arg VERSION=$VERSION .
