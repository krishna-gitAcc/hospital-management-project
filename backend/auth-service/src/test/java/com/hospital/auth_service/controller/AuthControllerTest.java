package com.hospital.auth_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.auth_service.config.JwtConfig;
import com.hospital.auth_service.config.SecurityConfig;
import com.hospital.auth_service.exception.UserAlreadyExistsException;
import com.hospital.auth_service.security.CustomUserDetailsService;
import com.hospital.auth_service.security.GatewayAccessFilter;
import com.hospital.auth_service.security.JwtAuthenticationFilter;
import com.hospital.auth_service.service.AuthService;
import com.hospital.common.dto.AuthRequest;
import com.hospital.common.dto.AuthResponse;
import com.hospital.common.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;

@WebMvcTest(controllers = AuthController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for unit testing controller logic
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private GatewayAccessFilter gatewayAccessFilter;

    @MockBean
    private JwtConfig jwtConfig;

    @Test
    @WithMockUser
    void register_Success() throws Exception {
        AuthRequest request = new AuthRequest("test@example.com", "password");
        AuthResponse response = AuthResponse.builder()
                .userId(UUID.randomUUID())
                .email("test@example.com")
                .role(UserRole.PATIENT)
                .build();

        when(authService.register(any(AuthRequest.class), any(UserRole.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())) // CSRF is disabled in config but good practice to include if needed
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser
    void register_UserAlreadyExists_ReturnsConflict() throws Exception {
        AuthRequest request = new AuthRequest("existing@example.com", "password");

        when(authService.register(any(AuthRequest.class), any(UserRole.class)))
                .thenThrow(new UserAlreadyExistsException("Email already exists"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }
}
