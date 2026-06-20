package com.mulemba.booksells.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static AuthenticatedUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AuthenticatedUser user) {
            return user;
        }
        throw new IllegalStateException("Utilizador não autenticado");
    }

    public static String getCurrentUserId() {
        return getCurrentUser().getUserId();
    }

    public static boolean isAdmin() {
        return getCurrentUser().getRole().name().equals("ADMIN");
    }
}
