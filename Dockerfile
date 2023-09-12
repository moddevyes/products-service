FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
EXPOSE 8002
ARG JAR_FILE=build/libs/products-service-0.0.1.jar
ARG JAR_FILE
COPY ${JAR_FILE} products-service-0.0.1.jar
ENTRYPOINT ["java","-jar","/products-service-0.0.1.jar"]


# BUILD
# docker build -t products-service .

# IMAGE built and in docker

# docker images
# REPOSITORY                    TAG             IMAGE ID       CREATED         SIZE
# products-service             latest          6977a8e7c2a4   4 minutes ago   386MB

# NETWORK for MySQL and Container
# docker network create products-netw

# RUN the container
# docker run -p8002:8002 products-service:latest

# INSPECT
# docker inspect message-server
# docker stop message-server
# docker rm message-server

