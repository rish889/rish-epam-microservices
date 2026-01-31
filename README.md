docker stop $(docker ps -q)
docker compose up --build

Monitoring Stack:
- Prometheus UI: http://localhost:9090
- Grafana UI: http://localhost:3000 (admin/admin)

minikube start --driver=docker
minikube status; kubectl cluster-info
bash k8s/deploy.sh
kubectl get pods -A
kubectl get pods -n microservices --show-labels

kubectl describe pod -n microservices -l app=postgres
kubectl logs -f -n microservices -l app=postgres
kubectl logs -n microservices -l app=postgres --tail=-1

kubectl logs -f -n microservices -l app=micro-sender &
kubectl logs -f -n microservices -l app=micro-recipient &
kubectl logs -f -n microservices -l app=micro-collector &
kubectl logs -f -n microservices -l app=micro-visualizer &
wait

kubectl port-forward -n microservices svc/micro-sender 8081:8081 &
kubectl port-forward -n microservices svc/micro-recipient 8082:8082 &
kubectl port-forward -n microservices svc/micro-collector 8083:8083 &
kubectl port-forward -n microservices svc/micro-visualizer 8084:8084 &
kubectl port-forward -n microservices svc/grafana 3000:3000 &
wait

bash k8s/canary-deploy.sh deploy
bash k8s/canary-deploy.sh status
bash k8s/canary-deploy.sh promote
bash k8s/canary-deploy.sh rollback

kubectl get pods -n microservices -l app=micro-collector
kubectl delete deployment micro-collector -n microservices
kubectl logs -n microservices -l app=micro-collector,version=canary -f
kubectl logs -n microservices -l app=micro-collector,version=stable -f
