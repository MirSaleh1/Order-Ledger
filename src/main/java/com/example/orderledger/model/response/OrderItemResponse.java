package com.example.orderledger.model.response;

import java.math.BigDecimal;

public record OrderItemResponse(Long productId,
                                String productName,
                                Integer quantity,
                                BigDecimal unitPrice,
                                BigDecimal subTotal) {
}
