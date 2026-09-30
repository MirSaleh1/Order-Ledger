package com.example.orderledger.model.response;

import java.time.LocalDateTime;

public record OrderStatusHistoryResponse(Long orderStatusHistory,
                                         Long orderId,
                                         String orderStatus,
                                         LocalDateTime localDateTime)
{
}
