docker stop $(docker ps -q)
docker compose up --build


Monitoring Stack:
- Prometheus UI: http://localhost:9090
- Grafana UI: http://localhost:3000 (admin/admin)




docker stop micro-sender;docker stop micro-collector;docker stop micro-recipient;docker stop rabbitmq
docker rm micro-sender;docker rm micro-collector;docker rm micro-recipient
docker rmi rish-epam-microservices_micro-sender ;docker rmi rish-epam-microservices_micro-recipient ;docker rmi rish-epam-microservices_micro-collector




minikube delete
docker system prune -af --volumes
systemctl --user restart docker
minikube start --driver=docker
minikube status; kubectl cluster-info
docker compose up --build
bash k8s/deploy.sh
kubectl get pods -A
minikube kubectl -- get pods -A


kubectl describe pod -n microservices -l app=rabbitmq
kubectl describe pod -n microservices -l app=prometheus


kubectl logs -f -n microservices -l app=micro-sender
kubectl logs -f -n microservices -l app=micro-recipient
kubectl logs -f -n microservices -l app=micro-collector


kubectl logs -f -l app=my-service


ping registry-1.docker.io
minikube ssh
sudo tee /etc/resolv.conf <<EOF
nameserver 8.8.8.8
nameserver 8.8.4.4
EOF
cat /etc/resolv.conf
exit


docker pull rabbitmq:3-management
minikube image load rabbitmq:3-management
docker pull prom/prometheus:latest
minikube image load prom/prometheus:latest


kubectl delete deployment rabbitmq -n microservices && kubectl apply -f /home/rishabh/workspace/rish-epam-microservices/k8s/rabbitmq.yaml






sudo vim /etc/docker/daemon.json
{
"dns": ["8.8.8.8", "8.8.4.4"]
}



bash k8s/deploy.sh stuck at
Waiting for deployments to be ready...
Waiting for RabbitMQ...
