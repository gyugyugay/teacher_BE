package com.teacher.qualification.component;

import com.teacher.qualification.service.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    public AuthorizationInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (accessToken == null || accessToken.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization 헤더가 없습니다");
            return false;
        }

        // 토큰에서 이메일 추출
        String email = jwtService.getEmailFromToken(accessToken);
        if (email == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다");
            return false;
        }

        // RequestContextHolder에 이메일 저장
        RequestContextHolder.getRequestAttributes().setAttribute("email", email, RequestAttributes.SCOPE_REQUEST);

        return true;
    }
}
