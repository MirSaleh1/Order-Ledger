package com.example.orderledger.mapper;

import com.example.orderledger.entity.Order;
import com.example.orderledger.entity.OrderItem;
import com.example.orderledger.entity.OrderStatusHistory;
import com.example.orderledger.entity.User;
import com.example.orderledger.model.response.OrderItemResponse;
import com.example.orderledger.model.response.OrderResponse;
import com.example.orderledger.model.response.OrderStatusHistoryResponse;
import com.example.orderledger.model.response.UserResponse;

import java.math.BigDecimal;

public interface OrderMapper {

    static UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(),  user.getUsername(), user.getEmail());
    }

    static OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        BigDecimal subTotal = orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
        return new OrderItemResponse(
                orderItem.getProduct().getId(),
                orderItem.getProduct().getName(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                subTotal
        );
    }

    static OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(order.getId(), order.getOrderStatus(),order.getTotalAmount(),
                order.getCreatedAt(),toUserResponse(order.getUser()),order.getItems().stream().
                map(OrderMapper::toOrderItemResponse).toList());
    }
    static OrderStatusHistoryResponse toOrderStatusHistoryResponse(OrderStatusHistory orderStatusHistory) {
        return new OrderStatusHistoryResponse(orderStatusHistory.getOrder().getId(), orderStatusHistory.getId(),
                orderStatusHistory.getStatus().toString(),orderStatusHistory.getCreatedAt());
    }
}
