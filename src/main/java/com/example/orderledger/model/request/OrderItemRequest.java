package com.example.orderledger.model.request;

public record OrderItemRequest(Long productId,
                               Integer quantity) {
}
