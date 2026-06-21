package com.mulemba.booksells.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class AuthenticatedUser {
    private final String userId;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;
}
