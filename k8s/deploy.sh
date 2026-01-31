#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

echo "=== Building Docker images ==="
echo "Configuring Minikube Docker environment..."
eval $(minikube docker-env)

echo "Building micro-sender..."
docker build -t micro-sender:latest "$PROJECT_DIR/micro-sender"

echo "Building micro-recipient..."
docker build -t micro-recipient:latest "$PROJECT_DIR/micro-recipient"

echo "Building micro-collector..."
docker build -t micro-collector:latest "$PROJECT_DIR/micro-collector"

echo "Building micro-visualizer..."
docker build -t micro-visualizer:latest "$PROJECT_DIR/micro-visualizer"

echo ""
echo "=== Deploying to Kubernetes ==="

echo "Creating namespace..."
kubectl apply -f "$SCRIPT_DIR/namespace.yaml"

echo "Deploying RabbitMQ..."
kubectl apply -f "$SCRIPT_DIR/rabbitmq.yaml"

echo "Deploying PostgreSQL..."
kubectl apply -f "$SCRIPT_DIR/postgres.yaml"

echo "Waiting for RabbitMQ to be ready..."
kubectl wait --namespace microservices --for=condition=ready pod -l app=rabbitmq --timeout=120s

echo "Waiting for PostgreSQL to be ready..."
kubectl wait --namespace microservices --for=condition=ready pod -l app=postgres --timeout=120s

echo "Deploying Prometheus configuration..."
kubectl apply -f "$SCRIPT_DIR/prometheus-config.yaml"

echo "Deploying Prometheus..."
kubectl apply -f "$SCRIPT_DIR/prometheus.yaml"

echo "Deploying Grafana configuration..."
kubectl apply -f "$SCRIPT_DIR/grafana-config.yaml"

echo "Deploying Grafana..."
kubectl apply -f "$SCRIPT_DIR/grafana.yaml"

echo "Deploying micro-sender..."
kubectl apply -f "$SCRIPT_DIR/micro-sender.yaml"

echo "Deploying micro-recipient..."
kubectl apply -f "$SCRIPT_DIR/micro-recipient.yaml"

echo "Deploying micro-collector..."
kubectl apply -f "$SCRIPT_DIR/micro-collector.yaml"

echo "Deploying micro-visualizer..."
kubectl apply -f "$SCRIPT_DIR/micro-visualizer.yaml"

echo ""
echo "=== Waiting for all pods to be ready ==="
kubectl wait --namespace microservices --for=condition=ready pod --all --timeout=180s

echo ""
echo "=== Deployment complete! ==="
echo ""
echo "Services:"
kubectl get services -n microservices
echo ""
echo "Pods:"
kubectl get pods -n microservices
echo ""
echo "To access services, use port-forward:"
echo "  kubectl port-forward -n microservices svc/micro-sender 8081:8081"
echo "  kubectl port-forward -n microservices svc/micro-recipient 8082:8082"
echo "  kubectl port-forward -n microservices svc/micro-collector 8083:8083"
echo "  kubectl port-forward -n microservices svc/micro-visualizer 8084:8084"
echo "  kubectl port-forward -n microservices svc/rabbitmq 15672:15672"
echo "  kubectl port-forward -n microservices svc/postgres 5432:5432"
echo "  kubectl port-forward -n microservices svc/prometheus 9090:9090"
echo "  kubectl port-forward -n microservices svc/grafana 3000:3000"
