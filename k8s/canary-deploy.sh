#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
NAMESPACE="microservices"

usage() {
    echo "Usage: $0 <service> {deploy|promote|rollback|status}"
    echo ""
    echo "Services: micro-recipient, micro-collector"
    echo ""
    echo "Commands:"
    echo "  deploy   - Build and deploy canary version"
    echo "  promote  - Promote canary to stable (full rollout)"
    echo "  rollback - Remove canary deployment"
    echo "  status   - Show current deployment status"
    echo ""
    echo "Examples:"
    echo "  $0 micro-recipient deploy"
    echo "  $0 micro-collector promote"
    exit 1
}

if [ -z "$1" ] || [ -z "$2" ]; then
    usage
fi

SERVICE="$1"
COMMAND="$2"

# Validate service name
if [[ "$SERVICE" != "micro-recipient" && "$SERVICE" != "micro-collector" ]]; then
    echo "Error: Invalid service '$SERVICE'"
    echo "Valid services: micro-recipient, micro-collector"
    exit 1
fi

deploy_canary() {
    echo "=== Deploying Canary Version for $SERVICE ==="

    echo "Configuring Minikube Docker environment..."
    eval $(minikube docker-env)

    echo "Building canary image..."
    docker build -t ${SERVICE}:canary "$PROJECT_DIR/${SERVICE}"

    echo "Deploying canary..."
    kubectl apply -f "$SCRIPT_DIR/${SERVICE}-canary.yaml"

    echo "Waiting for canary to be ready..."
    kubectl wait --namespace ${NAMESPACE} \
        --for=condition=ready pod \
        -l app=${SERVICE},version=canary \
        --timeout=120s

    echo ""
    echo "=== Canary Deployed ==="
    show_status
    echo ""
    echo "Monitor canary logs with:"
    echo "  kubectl logs -n ${NAMESPACE} -l app=${SERVICE},version=canary -f"
    echo ""
    echo "To promote: $0 $SERVICE promote"
    echo "To rollback: $0 $SERVICE rollback"
}

promote_canary() {
    echo "=== Promoting Canary to Stable for $SERVICE ==="

    # Check if canary exists
    if ! kubectl get deployment ${SERVICE}-canary -n ${NAMESPACE} &> /dev/null; then
        echo "Error: No canary deployment found for $SERVICE"
        exit 1
    fi

    echo "Configuring Minikube Docker environment..."
    eval $(minikube docker-env)

    echo "Tagging canary image as latest..."
    docker tag ${SERVICE}:canary ${SERVICE}:latest

    echo "Updating stable deployment..."
    kubectl set image deployment/${SERVICE} \
        ${SERVICE}=${SERVICE}:latest \
        -n ${NAMESPACE}

    echo "Waiting for stable rollout..."
    kubectl rollout status deployment/${SERVICE} -n ${NAMESPACE}

    echo "Removing canary deployment..."
    kubectl delete deployment ${SERVICE}-canary -n ${NAMESPACE}

    echo ""
    echo "=== Promotion Complete ==="
    show_status
}

rollback_canary() {
    echo "=== Rolling Back Canary for $SERVICE ==="

    if kubectl get deployment ${SERVICE}-canary -n ${NAMESPACE} &> /dev/null; then
        kubectl delete deployment ${SERVICE}-canary -n ${NAMESPACE}
        echo "Canary deployment removed"
    else
        echo "No canary deployment found"
    fi

    echo ""
    show_status
}

show_status() {
    echo "=== Current Deployment Status for $SERVICE ==="
    echo ""
    echo "Deployments:"
    kubectl get deployments -n ${NAMESPACE} -l app=${SERVICE} -o wide
    echo ""
    echo "Pods:"
    kubectl get pods -n ${NAMESPACE} -l app=${SERVICE} -o wide
    echo ""
    echo "Traffic split (by replica count):"
    STABLE=$(kubectl get deployment ${SERVICE} -n ${NAMESPACE} -o jsonpath='{.spec.replicas}' 2>/dev/null || echo "0")
    CANARY=$(kubectl get deployment ${SERVICE}-canary -n ${NAMESPACE} -o jsonpath='{.spec.replicas}' 2>/dev/null || echo "0")
    TOTAL=$((STABLE + CANARY))
    if [ "$TOTAL" -gt 0 ]; then
        STABLE_PCT=$((STABLE * 100 / TOTAL))
        CANARY_PCT=$((CANARY * 100 / TOTAL))
        echo "  Stable: ${STABLE} replicas (~${STABLE_PCT}% traffic)"
        echo "  Canary: ${CANARY} replicas (~${CANARY_PCT}% traffic)"
    else
        echo "  No deployments found"
    fi
}

case "$COMMAND" in
    deploy)
        deploy_canary
        ;;
    promote)
        promote_canary
        ;;
    rollback)
        rollback_canary
        ;;
    status)
        show_status
        ;;
    *)
        usage
        ;;
esac
