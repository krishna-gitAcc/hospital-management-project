package com.hospital.auth_service.controller;

import com.hospital.auth_service.service.AuthService;
import com.hospital.common.dto.AuthRequest;
import com.hospital.common.dto.AuthResponse;
import com.hospital.common.enums.UserRole;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest request, @RequestParam(defaultValue = "PATIENT")UserRole role){
        return ResponseEntity.ok(authService.register(request, role));
    }

    @PostMapping("/login/jwt")
    public ResponseEntity<AuthResponse> loginJwt(@Valid @RequestBody AuthRequest request){
        return ResponseEntity.ok(authService.loginJwt(request));
    }

    @PostMapping("/login/basic")
    public ResponseEntity<AuthResponse> loginBasic(@Valid @RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(authService.loginBasic(authRequest));
    }

    @PostMapping("/login/session")
    public ResponseEntity<AuthResponse> loginSession(@Valid @RequestBody AuthRequest request){
        return ResponseEntity.ok(authService.loginSession(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
        return ResponseEntity.ok().build();
    }

    @Setter
    @Getter
    public static class RefreshTokenRequest {
        private String refreshToken;
    }

}
