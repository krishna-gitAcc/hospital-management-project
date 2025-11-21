package com.hospital.auth_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class GatewayAccessFilter extends OncePerRequestFilter {

    private static final String GATEWAY_SECRET_HEADER = "X-Gateway-Secret";
//    @Value("${gateway.secret}")
    private static final String GATEWAY_SECRET_VALUE = "hospital-secret-key";// In production, use @Value("${gateway.secret}")

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String secretHeader = request.getHeader(GATEWAY_SECRET_HEADER);

        if (secretHeader == null || !secretHeader.equals(GATEWAY_SECRET_VALUE)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access Denied: Only Gateway can access this service");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
