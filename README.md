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
minikube start
minikube status; kubectl cluster-info

ping registry-1.docker.io
minikube ssh
sudo tee /etc/resolv.conf <<EOF
nameserver 8.8.8.8
nameserver 8.8.4.4
EOF
cat /etc/resolv.conf
exit

bash k8s/deploy.sh
kubectl get pods -A
minikube kubectl -- get pods -A




kubectl describe pod -n microservices -l app=rabbitmq
kubectl describe pod -n microservices -l app=prometheus
kubectl describe pod -n microservices -l app=micro-sender

kubectl logs -f -n microservices -l app=rabbitmq
kubectl logs -f -n microservices -l app=micro-sender
kubectl logs -f -n microservices -l app=micro-recipient
kubectl logs -f -n microservices -l app=micro-collector


kubectl port-forward -n microservices svc/micro-sender 8081:8081 &
kubectl port-forward -n microservices svc/micro-recipient 8082:8082 &
kubectl port-forward -n microservices svc/micro-collector 8083:8083 &
kubectl port-forward -n microservices svc/grafana 3000:3000 &


kubectl apply -f k8s/rabbitmq.yaml

kubectl delete pod micro-collector-6b878c6499-nk6vd -n microservices
kubectl delete pod micro-recipient-7598588c58-lxb7b -n microservices
kubectl delete pod micro-recipient-7f9cf7b7b7-lp8wp -n microservices
kubectl delete pod micro-sender-69fff5df77-b4cnc -n microservices
kubectl delete pod micro-sender-85c55c5864-s57d2 -n microservices


micro-collector-6b878c6499-nk6vd   
micro-recipient-7598588c58-lxb7b   
micro-recipient-7f9cf7b7b7-lp8wp   
micro-sender-69fff5df77-b4cnc      
micro-sender-85c55c5864-s57d2      


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



































minikube start
eval $(minikube docker-env) && docker build -t micro-sender:latest /home/rishabh/workspace/rish-epam-microservices/micro-sender
docker build -t micro-sender:latest ./micro-sender
