package com.example.order_service.controller;

import com.example.order_service.Client.ProductClient;
import com.example.order_service.dto.OrderDto;
import com.example.order_service.dto.ProductDto;
import com.example.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final ProductClient productClient;

    private final Environment env;

    @Autowired
    public OrderController(OrderService orderService, ProductClient productClient, Environment env) {
        this.orderService = orderService;
        this.productClient = productClient;
        this.env = env;
    }

    @GetMapping
    public List<OrderDto> getAllOrders() {
        System.out.println(env.getProperty("local.server.port"));
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackGetProductById")
    public OrderDto getOrderById(@PathVariable Long id) {
        ProductDto product = productClient.getProductById(id);

        System.out.println(env.getProperty("local.server.port"));

        return new OrderDto(id, product.getName(), 3);
    }
    @PostMapping
    public OrderDto createOrder(@RequestBody OrderDto orderDto) {
        System.out.println(env.getProperty("local.server.port"));
        return orderService.createOrder(orderDto);
    }

    @PutMapping("/{id}")
    public OrderDto updateOrder(@PathVariable Long id, @RequestBody OrderDto orderDto) {
        return orderService.updateOrder(id, orderDto);
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "Order deleted successfully";
    }



    public OrderDto fallbackGetProductById(Long id, Throwable throwable) {
        System.out.println("Fallback triggered for order id: " + id + " -> reason: " + throwable.getMessage());
        return new OrderDto(id, "Fallback Product", 1);
    }
}
