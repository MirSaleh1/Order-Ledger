package com.example.orderledger.model.request;

import java.util.List;

public record CreateOrderRequest(Long userId,
                                 List<OrderItemRequest> orderItemRequests,
                                 String couponCode) {
}
