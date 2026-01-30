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

kubectl logs -n microservices -f micro-collector-8d6bc4dc6-nf65r &
kubectl logs -n microservices -f micro-recipient-7f9cf7b7b7-nll8j &
kubectl logs -n microservices -f micro-sender-69fff5df77-88bvt &
wait


kubectl port-forward -n microservices svc/micro-sender 8081:8081 &
kubectl port-forward -n microservices svc/micro-recipient 8082:8082 &
kubectl port-forward -n microservices svc/micro-collector 8083:8083 &
kubectl port-forward -n microservices svc/grafana 3000:3000 &

curl -fL -o stern.tar.gz https://github.com/stern/stern/releases/download/v1.33.1/stern_1.33.1_linux_amd64.tar.gz
tar -xzf stern.tar.gz
sudo mv stern /usr/local/bin/
stern --version