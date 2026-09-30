package com.example.orderledger.model.response;

import com.example.orderledger.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(Long orderId,
                            OrderStatus status,
                            BigDecimal totalAmount,
                            LocalDateTime createdAt,
                            UserResponse user,
                            List<OrderItemResponse> items) {
}
