package com.example.orderledger.model.request;

import java.math.BigDecimal;

public record ProductRequest(String name,
                             BigDecimal price,
                             Integer stockQuantity) {
}
