package com.mulemba.booksells.dto;

public record AuthResponse(
        String token,
        UserResponse user
) {}
