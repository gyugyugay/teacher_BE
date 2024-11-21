package com.teacher.qualification.service.jwt;

import com.teacher.qualification.configuration.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtConfig jwtConfig;

    public String generateToken(String email) {
        return jwtConfig.generateToken(email);
    }

    public boolean validateToken(String token, String email) {
        return jwtConfig.validateToken(token, email);
    }

    public String getEmailFromToken(String token) {
        return jwtConfig.extractEmail(token);
    }

}