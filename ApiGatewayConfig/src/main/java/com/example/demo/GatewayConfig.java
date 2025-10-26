package com.example.demo;

import org.springframework.cloud.gateway.route.RouteLocator;

import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // ✅ Route for Order Microservice — uses Eureka for load balancing
                .route("ORDER-MICROSERVICE", r -> r
                        .path("/orders/**")
                        .uri("lb://ORDER-MICROSERVICE"))

                // ✅ Route for Product Microservice
                .route("PRODUCT-MICROSERVICE", r -> r
                        .path("/products/**")
                        .uri("lb://PRODUCT-MICROSERVICE"))
                .build();
    }



}
