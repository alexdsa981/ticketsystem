package com.ipor.ticketsystem.core.security;

import com.ipor.ticketsystem.core.other.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Eliminar la cookie del token JWT
        CookieUtil.removeJwtCookie(response);
        //enviar al login si se existe una exepcion
        response.sendRedirect("/login");
    }
}
