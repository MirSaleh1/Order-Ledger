package com.example.orderledger.model.response;

import java.math.BigDecimal;

public record ProductResponse(Long id,
                              String name,
                              BigDecimal price,
                              Integer stockQuantity) {
}
