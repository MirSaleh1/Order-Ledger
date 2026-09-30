package com.example.orderledger.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionConstants {
    USER_NOT_FOUND("İstifadəçi tapılmadı", 404),
    PRODUCT_NOT_FOUND("Məhsul tapılmadı", 404),
    PRODUCT_ALREADY_EXISTS("Bu adlı məhsul artıq mövcuddur", 409),
    INSUFFICIENT_STOCK("Kifayət qədər stok yoxdur", 400),
    ORDER_NOT_FOUND("Sifariş tapılmadı", 404),
    ORDER_ALREADY_IN_STATUS("Sifariş artıq bu statusdadır!", 400),
    FINISHED_ORDER_CANNOT_BE_CHANGED("Bitmiş və ya ləğv edilmiş sifarişin statusunu dəyişmək olmaz!", 400),
    ONLY_NEW_ORDER_CAN_BE_PAID("Yalnız yeni yaradılmış sifariş ödənilə bilər!", 400),
    ONLY_PAID_ORDER_CAN_BE_SHIPPED("Yalnız ödənilmiş sifariş kuryerə verilə bilər (SHIPPED)!", 400),
    ONLY_SHIPPED_ORDER_CAN_BE_COMPLETED("Yalnız göndərilmiş sifariş tamamlana bilər (COMPLETED)!", 400),
    FINISHED_ORDER_CANNOT_BE_CANCELLED("Bitmiş və ya ləğv edilmiş sifarişi ləğv etmək olmaz!", 400),
    COUPON_NOT_FOUND("Kupon tapılmadı", 404),
    COUPON_INACTIVE("Bu kupon artıq aktiv deyil", 400),
    COUPON_EXPIRED("Kuponun vaxtı bitib", 400),
    COUPON_LIMIT_REACHED("Kuponun istifadə limiti dolub", 400);

    private final String message;
    private final int statusCode;
}