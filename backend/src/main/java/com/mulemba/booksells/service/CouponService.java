package com.mulemba.booksells.service;

import com.mulemba.booksells.dto.CouponValidationResponse;
import com.mulemba.booksells.exception.BusinessException;
import com.mulemba.booksells.model.PromoCode;
import com.mulemba.booksells.repository.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final PromoCodeRepository promoCodeRepository;

    public CouponValidationResponse validate(String code, BigDecimal subtotal) {
        PromoCode promo = promoCodeRepository.findById(code.toUpperCase().trim())
                .orElseThrow(() -> new BusinessException("Cupom inválido"));

        if (!promo.isActive()) {
            throw new BusinessException("Cupom expirado ou inactivo");
        }

        BigDecimal discount;
        if (promo.isPercentage()) {
            discount = subtotal.multiply(promo.getDiscountValue()).setScale(2, RoundingMode.HALF_UP);
        } else {
            discount = promo.getDiscountValue().min(subtotal);
        }

        return new CouponValidationResponse(code, discount, true);
    }

    public BigDecimal calculateDiscount(String code, BigDecimal subtotal) {
        if (code == null || code.isBlank()) return BigDecimal.ZERO;
        try {
            return validate(code, subtotal).discount();
        } catch (BusinessException e) {
            return BigDecimal.ZERO;
        }
    }
}
