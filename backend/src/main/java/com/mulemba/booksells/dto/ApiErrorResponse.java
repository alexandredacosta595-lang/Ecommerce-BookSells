package com.mulemba.booksells.dto;

public record ApiErrorResponse(
        String message,
        int status
) {}
