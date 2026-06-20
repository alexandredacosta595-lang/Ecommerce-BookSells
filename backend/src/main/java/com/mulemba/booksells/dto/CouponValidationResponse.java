package com.mulemba.booksells.dto;

import java.math.BigDecimal;

public record CouponValidationResponse(
        String code,
        BigDecimal discount,
        boolean valid
) {}
