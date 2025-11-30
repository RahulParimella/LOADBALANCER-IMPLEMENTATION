# JWT Authentication Implementation Documentation

## Overview
This document details the changes made to replace OAuth 2.0 with **JWT (JSON Web Token) Authentication** in the ApiGateway.

## Architecture Changes
- **Authentication Provider**: Switched from GitHub OAuth2 to a custom JWT implementation.
- **Token Management**: The ApiGateway now issues and validates JWT tokens.
- **User Store**: Currently using a hardcoded user (`admin`/`password`) for demonstration.

## Detailed File Changes

### 1. ApiGatewayConfig Service

#### `pom.xml`
- **Removed**: `spring-boot-starter-oauth2-client`
- **Added**:
    - `spring-boot-starter-security`
    - `jjwt-api`
    - `jjwt-impl`
    - `jjwt-jackson`

#### `src/main/java/com/example/demo/util/JwtUtil.java` **(New)**
- Utility class for:
    - Generating tokens (`generateToken`)
    - Validating tokens (`validateToken`)
    - Extracting claims

#### `src/main/java/com/example/demo/dto/` **(New)**
- `AuthRequest.java`: DTO for login credentials.
- `AuthResponse.java`: DTO for returning the JWT token.

#### `src/main/java/com/example/demo/controller/AuthController.java` **(New)**
- Exposes `POST /auth/login`.
- Validates credentials (hardcoded `admin`/`password`).
- Returns a JWT token on success.

#### `src/main/java/com/example/demo/filter/AuthenticationFilter.java` **(New)**
- A custom Gateway Filter.
- Intercepts requests to protected routes.
- Checks for `Authorization: Bearer <token>` header.
- Validates the token using `JwtUtil`.
- Throws 401 if invalid.

#### `src/main/java/com/example/demo/filter/RouteValidator.java` **(New)**
- Helper to identify open endpoints (like `/auth/login`) that should bypass the filter.

#### `src/main/java/com/example/demo/config/SecurityConfig.java` **(New)**
- Disables CSRF.
- Permits all requests at the Spring Security level (because `AuthenticationFilter` handles the actual security for specific routes).

#### `src/main/java/com/example/demo/GatewayConfig.java` **(Modified)**
- Injected `AuthenticationFilter`.
- Applied the filter to `ORDER-MICROSERVICE` and `PRODUCT-MICROSERVICE` routes.

## How to Test

1.  **Start the Stack**:
    ```bash
    docker-compose up -d --build
    ```

2.  **Login**:
    - **URL**: `POST http://localhost:9191/auth/login`
    - **Body**:
        ```json
        {
            "username": "admin",
            "password": "password"
        }
        ```
    - **Response**:
        ```json
        {
            "accessToken": "eyJhbGciOiJIUzI1NiJ9..."
        }
        ```

3.  **Access Protected Resource**:
    - **URL**: `GET http://localhost:9191/order-service/orders`
    - **Header**: `Authorization: Bearer <your_token>`
