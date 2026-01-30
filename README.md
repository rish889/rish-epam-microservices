docker stop $(docker ps -q)
docker compose up --build

Monitoring Stack:
- Prometheus UI: http://localhost:9090
- Grafana UI: http://localhost:3000 (admin/admin)

minikube start --driver=docker
minikube status; kubectl cluster-info
bash k8s/deploy.sh
kubectl get pods -A

kubectl describe pod -n microservices -l app=micro-sender

kubectl logs -n microservices -f micro-collector-8d6bc4dc6-nf65r &
kubectl logs -n microservices -f micro-recipient-7f9cf7b7b7-nll8j &
kubectl logs -n microservices -f micro-sender-69fff5df77-88bvt &
wait

kubectl port-forward -n microservices svc/micro-sender 8081:8081 &
kubectl port-forward -n microservices svc/micro-recipient 8082:8082 &
kubectl port-forward -n microservices svc/micro-collector 8083:8083 &
kubectl port-forward -n microservices svc/grafana 3000:3000 &
wait
