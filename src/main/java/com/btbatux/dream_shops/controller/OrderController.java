package com.btbatux.dream_shops.controller;

import com.btbatux.dream_shops.exception.ResourceNotFoundException;
import com.btbatux.dream_shops.model.Order;
import com.btbatux.dream_shops.model.User;
import com.btbatux.dream_shops.response.ApiResponse;
import com.btbatux.dream_shops.service.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@RestController //RESTful web hizmetleri
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
        try {
            Order order = orderService
                    .placeOrder(userId); //Sipariş verir 1.
            return ResponseEntity
                    .ok(new ApiResponse("Success Oder ", order));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error", e.getMessage()));
        }
    }

    @GetMapping("/{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrder(orderId);
            return ResponseEntity
                    .ok(new ApiResponse("Success Order ", order));
        } catch (ResourceNotFoundException r) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse("Oopss!", r.getMessage()));
        }

    }


    @GetMapping("/{userId}/orders")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        try {
            List<Order> orderList = orderService.getUserOrders(userId);
            return ResponseEntity
                    .ok(new ApiResponse("Success Order List ", orderList));
        } catch (ResourceNotFoundException r) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse("Oopss!", r.getMessage()));
        }

    }
}

