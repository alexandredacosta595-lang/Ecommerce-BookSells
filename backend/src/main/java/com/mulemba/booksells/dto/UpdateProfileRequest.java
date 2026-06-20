package com.mulemba.booksells.dto;

public record UpdateProfileRequest(
        String name,
        String bio,
        String avatar,
        String companyName,
        String city,
        String state,
        String phone,
        String whatsapp,
        String website
) {}
