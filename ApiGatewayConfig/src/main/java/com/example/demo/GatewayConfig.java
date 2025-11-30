package com.example.demo;

import com.example.demo.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;

import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

        @Autowired
        private AuthenticationFilter filter;

        @Bean
        public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
                return builder.routes()
                                // ✅ Route for Order Microservice — uses Eureka for load balancing
                                .route("ORDER-MICROSERVICE", r -> r
                                                .path("/orders/**")
                                                .filters(f -> f.filter(filter.apply(new AuthenticationFilter.Config())))
                                                .uri("lb://ORDER-MICROSERVICE"))

                                // ✅ Route for Product Microservice
                                .route("PRODUCT-MICROSERVICE", r -> r
                                                .path("/products/**")
                                                .filters(f -> f.filter(filter.apply(new AuthenticationFilter.Config())))
                                                .uri("lb://PRODUCT-MICROSERVICE"))
                                .build();
        }

}
