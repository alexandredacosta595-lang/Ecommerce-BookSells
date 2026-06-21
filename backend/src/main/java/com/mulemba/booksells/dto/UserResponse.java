package com.mulemba.booksells.dto;

import com.mulemba.booksells.model.Role;
import com.mulemba.booksells.model.enums.UserType;

public record UserResponse(
        String id,
        String name,
        String email,
        String role,
        String avatar,
        String memberSince,
        String bio,
        String userType,
        String companyName,
        String city,
        String state,
        String phone,
        String whatsapp,
        String website
) {
    public static UserResponse from(com.mulemba.booksells.model.User user) {
        boolean isAdmin = user.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN"));
        
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                isAdmin ? "admin" : "user",
                user.getAvatar(),
                user.getMemberSince().toString(),
                user.getBio(),
                user.getUserType() != null ? user.getUserType().name().toLowerCase() : null,
                user.getCompanyName(),
                user.getCity(),
                user.getState(),
                user.getPhone(),
                user.getWhatsapp(),
                user.getWebsite()
        );
    }
}
