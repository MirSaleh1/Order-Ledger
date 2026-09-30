package com.example.orderledger.service;

import com.example.orderledger.entity.Coupon;
import com.example.orderledger.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import com.example.orderledger.exception.BusinessException;
import static com.example.orderledger.exception.ExceptionConstants.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional
    public BigDecimal applyCoupon(String code, BigDecimal originalTotal) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new BusinessException(COUPON_NOT_FOUND));
        if (!coupon.isActive()) {
            throw new BusinessException(COUPON_INACTIVE);
        }
        if (coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BusinessException(COUPON_EXPIRED);
        }
        if (coupon.getUsedCount() >= coupon.getUsageLimit()) {
            throw new BusinessException(COUPON_LIMIT_REACHED);
        }
        BigDecimal discountMultiplier = BigDecimal.valueOf(100).subtract(coupon.getDiscountPercentage())
                .divide(BigDecimal.valueOf(100));

        BigDecimal discountedTotal = originalTotal.multiply(discountMultiplier);
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepository.save(coupon);

        return discountedTotal;
    }
}
