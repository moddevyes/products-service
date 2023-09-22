#!/bin/bash

# DATABASE
echo ""
echo "Creating PRODUCTS database."
echo ""

docker run --name products_mysql_container -d -p 3302:3306 -e MYSQL_ROOT_PASSWORD_FILE=/run/secrets/mysql-root-password \
-e MYSQL_DATABASE=ecommerce_products_db \
-e MYSQL_USER=davidking \
-e MYSQL_PASSWORD=davidking!! \
--env-file config/.env.dev -v ./secrets:/run/secrets --network commerce-net --restart unless-stopped mysql:8.0.1

# DOCKER IMAGE
echo ""
echo "Building DOCKER image for products-service."
echo ""
docker build -t products-service .

echo ""
echo "Deploying/Running DOCKER image for products-service."
echo ""
docker run -d --env-file config/.env.dev --name=products_service_container --net=commerce-net -p 8002:8002 --restart unless-stopped products-service

# VERIFY
echo ""
echo "VERIFY deployment"
echo ""
docker ps
echo ""
echo ""
docker images
