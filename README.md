docker stop $(docker ps -q)
docker compose build
docker compose up
















docker stop micro-sender;docker stop micro-collector;docker stop micro-recipient;docker stop rabbitmq
docker rm micro-sender;docker rm micro-collector;docker rm micro-recipient
docker rmi rish-epam-microservices_micro-sender ;docker rmi rish-epam-microservices_micro-recipient ;docker rmi rish-epam-microservices_micro-collector