package com.mulemba.booksells.service;

import com.mulemba.booksells.dto.*;
import com.mulemba.booksells.exception.BusinessException;
import com.mulemba.booksells.model.Role;
import com.mulemba.booksells.model.User;
import com.mulemba.booksells.model.enums.UserType;
import com.mulemba.booksells.repository.RoleRepository;
import com.mulemba.booksells.repository.UserRepository;
import com.mulemba.booksells.security.AuthenticatedUser;
import com.mulemba.booksells.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest validRegisterRequest;
    private LoginRequest validLoginRequest;
    private User activeUser;

    @BeforeEach
    void setUp() {
        validRegisterRequest = new RegisterRequest("Teste User", "test@gmail.com", "password123", UserType.READER, null, null, null, null, null);
        validLoginRequest = new LoginRequest("test@gmail.com", "password123");

        Role userRole = new Role();
        userRole.setName("ROLE_USER");

        activeUser = User.builder()
                .id("usr-123")
                .name("Teste User")
                .email("test@gmail.com")
                .password("encoded_password")
                .roles(Set.of(userRole))
                .memberSince(java.time.LocalDate.now())
                .enabled(true)
                .build();
    }

    @Test
    void register_ShouldThrowException_WhenEmailExists() {
        when(userRepository.existsByEmail(validRegisterRequest.email())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> authService.register(validRegisterRequest));
        assertEquals("E-mail já registado", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_ShouldThrowException_WhenCompanyNameMissingForBookstore() {
        RegisterRequest request = new RegisterRequest("Bookstore", "book@gmail.com", "pass", UserType.BOOKSTORE, null, null, null, null, null);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class, () -> authService.register(request));
        assertEquals("Nome da empresa é obrigatório para livrarias e editoras", exception.getMessage());
    }

    @Test
    void register_ShouldReturnAuthResponse_WhenValidRequest() {
        Role userRole = new Role();
        userRole.setName("ROLE_USER");

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(roleRepository.findByName("ROLE_SELLER")).thenReturn(Optional.of(new Role()));
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_pass");
        when(jwtService.generateToken(any(AuthenticatedUser.class))).thenReturn("fake_token");

        AuthResponse response = authService.register(validRegisterRequest);

        assertNotNull(response);
        assertEquals("fake_token", response.token());
        assertEquals("test@gmail.com", response.user().email());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void login_ShouldReturnAuthResponse_WhenCredentialsValid() {
        when(userRepository.findByEmail(validLoginRequest.email())).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches(validLoginRequest.password(), activeUser.getPassword())).thenReturn(true);
        when(jwtService.generateToken(any(AuthenticatedUser.class))).thenReturn("fake_jwt_token");

        AuthResponse response = authService.login(validLoginRequest);

        assertNotNull(response);
        assertEquals("fake_jwt_token", response.token());
        assertEquals("test@gmail.com", response.user().email());
    }

    @Test
    void login_ShouldThrowBadCredentials_WhenPasswordIncorrect() {
        when(userRepository.findByEmail(validLoginRequest.email())).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches(validLoginRequest.password(), activeUser.getPassword())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authService.login(validLoginRequest));
    }

    @Test
    void login_ShouldThrowBusinessException_WhenUserDisabled() {
        activeUser.setEnabled(false);
        when(userRepository.findByEmail(validLoginRequest.email())).thenReturn(Optional.of(activeUser));

        assertThrows(BusinessException.class, () -> authService.login(validLoginRequest));
    }
}
