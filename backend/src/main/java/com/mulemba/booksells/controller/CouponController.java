package com.mulemba.booksells.controller;

import com.mulemba.booksells.dto.CouponValidationResponse;
import com.mulemba.booksells.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/validate")
    public CouponValidationResponse validate(@RequestBody Map<String, Object> body) {
        String code = (String) body.get("code");
        BigDecimal subtotal = new BigDecimal(body.get("subtotal").toString());
        return couponService.validate(code, subtotal);
    }
}
