# Dockerization Documentation for Load Balancer Application

## 1. Overview
This document outlines the changes made and new files added to Dockerize the Load Balancer application. The application consists of four microservices: `eurekaserver`, `ApiGatewayConfig`, `order-service`, and `product-service`, along with a PostgreSQL database.

## 2. Changes Made to Existing Code
The original source code and `application.properties` files were **not modified** to hardcode Docker configurations. Instead, environment variables in `docker-compose.yml` are used to override the default local configurations (like `localhost`) with Docker service names. This ensures the application can still run locally without Docker if needed.

**Key Configuration Overrides:**
- **Database Connection**: The `localhost` address for PostgreSQL was replaced with `postgres` (the container name) via `SPRING_DATASOURCE_URL`.
- **Eureka Server**: The `localhost` address for the Eureka server was replaced with `eureka-server` (the container name) via `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE`.

## 3. New Files Added
The following files were created to enable Docker support:

### 3.1 Dockerfiles
A `Dockerfile` was added to each microservice directory to define how to build its Docker image. All Dockerfiles use a multi-stage build process (Build stage with Maven, Run stage with JRE Alpine) for optimized image size.

- **`ApiGatewayConfig/Dockerfile`**: Builds the API Gateway image.
- **`eurekaserver/Dockerfile`**: Builds the Eureka Server image.
- **`order-service/Dockerfile`**: Builds the Order Service image.
- **`product-service/Dockerfile`**: Builds the Product Service image.

### 3.2 docker-compose.yml
A `docker-compose.yml` file was added to the root directory to orchestrate all services. It defines:
- **Services**: `eureka-server`, `api-gateway`, `postgres`, `order-service`, `product-service`.
- **Networks**: A custom bridge network `mynet` to allow services to communicate.
- **Volumes**: Persists database data and initializes schemas.
- **Healthchecks**: Ensures services start in the correct order (e.g., services wait for `eureka-server` and `postgres` to be healthy).

### 3.3 init.sql
An `init.sql` file was added to the root directory to automatically create the required databases (`orderdb` and `productdb`) when the PostgreSQL container starts.

## 4. Docker Commands Used

### Build and Start
To build the images and start all services in the background:
```bash
docker-compose up -d --build
```

### Stop Services
To stop all running services and remove containers:
```bash
docker-compose down
```

### View Logs
To view logs for all services:
```bash
docker-compose logs -f
```

To view logs for a specific service (e.g., order-service):
```bash
docker-compose logs -f order-service
```

### Check Status
To see the status of running containers:
```bash
docker-compose ps
```

## 5. Service Configuration Details

### Eureka Server
- **Port**: 8761
- **Network Alias**: `eureka-server`

### API Gateway
- **Port**: 9191
- **Depends On**: `eureka-server`

### PostgreSQL
- **Port**: 5432
- **Database Names**: `orderdb`, `productdb` (created via `init.sql`)
- **Credentials**: User `postgres`, Password `test123`

### Order Service
- **Port**: 8081
- **Depends On**: `postgres`, `eureka-server`
- **Database**: Connects to `orderdb`

### Product Service
- **Port**: 8083
- **Depends On**: `postgres`, `eureka-server`
- **Database**: Connects to `productdb`
..