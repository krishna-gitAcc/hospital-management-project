package com.hospital.auth_service.service;

import com.hospital.auth_service.config.JwtConfig;
import com.hospital.auth_service.entity.User;
import com.hospital.auth_service.exception.UserAlreadyExistsException;
import com.hospital.auth_service.repository.UserRepository;
import com.hospital.common.dto.AuthRequest;
import com.hospital.common.dto.AuthResponse;
import com.hospital.common.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_Success() {
        // Arrange
        AuthRequest request = new AuthRequest("test@example.com", "password");
        UserRole role = UserRole.PATIENT;
        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail())
                .role(role)
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        AuthResponse response = authService.register(request, role);

        // Assert
        assertNotNull(response);
        assertEquals(savedUser.getId(), response.getUserId());
        assertEquals(savedUser.getEmail(), response.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_UserAlreadyExists_ThrowsException() {
        // Arrange
        AuthRequest request = new AuthRequest("test@example.com", "password");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> authService.register(request, UserRole.PATIENT));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginJwt_Success() {
        // Arrange
        AuthRequest request = new AuthRequest("test@example.com", "password");
        User user = User.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail())
                .password("password") // Ensure password is set
                .role(UserRole.PATIENT)
                .build();
        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), java.util.Collections.emptyList());
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        lenient().when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtConfig.generateToken(user.getEmail(), user.getRole().name())).thenReturn("jwt-token");
        when(jwtConfig.generateRefreshToken(user.getEmail())).thenReturn("refresh-token");

        // Act
        AuthResponse response = authService.loginJwt(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("refresh-token", response.getRefreshToken());
    }
}
