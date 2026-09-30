package com.example.orderledger.controller;

import com.example.orderledger.model.request.CreateOrderRequest;
import com.example.orderledger.model.response.OrderResponse;
import com.example.orderledger.model.OrderStatus;
import com.example.orderledger.model.response.OrderStatusHistoryResponse;
import com.example.orderledger.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        return orderService.createOrder(createOrderRequest);
    }

    @PatchMapping("/{id}/status")
    public OrderResponse changeOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return orderService.changeOrderStatus(id, status);
    }

    @PostMapping("/{id}/cancel")
    public OrderResponse cancelOrder(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponse> getOrdersByUserId(@PathVariable Long userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @GetMapping("/{id}/history")
    public List<OrderStatusHistoryResponse> getOrderStatusHistory(@PathVariable Long id) {
        return orderService.getOrderStatusHistory(id);
    }
}
