package com.hospital.auth_service.service;


import com.hospital.auth_service.config.JwtConfig;
import com.hospital.auth_service.entity.User;
import com.hospital.auth_service.repository.UserRepository;

import com.hospital.common.dto.AuthRequest;
import com.hospital.common.dto.AuthResponse;
import com.hospital.common.enums.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hospital.auth_service.exception.UserAlreadyExistsException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final JwtConfig jwtConfig;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(AuthRequest request, UserRole role){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new UserAlreadyExistsException("Email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .active(true)
                .build();
        user = userRepository.save(user);

        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public AuthResponse loginJwt(AuthRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException("User not found"));

        String token = jwtConfig.generateToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtConfig.generateRefreshToken(user.getEmail());

        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .refreshToken(refreshToken)
                .build();

    }

    public AuthResponse loginBasic(AuthRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new RuntimeException("User not found"));

        String token = jwtConfig.generateToken(user.getEmail(), user.getRole().name());
        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .build();
    }

    public AuthResponse loginSession(AuthRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new RuntimeException("User not found"));

        String sessionId = SecurityContextHolder.getContext().getAuthentication().getName();

        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .sessionId(sessionId)
                .build();
    }

    public AuthResponse refreshToken(String refreshToken){
        String email = jwtConfig.extractEmail(refreshToken);

        if(!jwtConfig.validateToken(refreshToken, email)){
            throw new RuntimeException("Invalid refresh token");
        }

        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));

        String newToken = jwtConfig.generateToken(user.getEmail(), user.getRole().name());
        String newRefreshToken = jwtConfig.generateRefreshToken(user.getEmail());

        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .token(newToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
