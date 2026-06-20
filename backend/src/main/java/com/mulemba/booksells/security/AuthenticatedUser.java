package com.mulemba.booksells.security;

import com.mulemba.booksells.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class AuthenticatedUser {
    private final String userId;
    private final String email;
    private final UserRole role;
}
