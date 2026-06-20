package com.mulemba.booksells.dto;

import java.math.BigDecimal;

public record AdminStatsResponse(
        long totalBooks,
        long totalOrders,
        long totalUsers,
        BigDecimal totalRevenue,
        long pendingOrders
) {}
