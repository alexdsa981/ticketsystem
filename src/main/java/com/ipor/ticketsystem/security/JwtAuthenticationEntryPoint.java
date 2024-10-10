package com.ipor.ticketsystem.security;

import com.ipor.ticketsystem.service.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

//clase para poder manejar las excepciones de tipo autenticacion en nuestra app
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        System.out.println(authException.getMessage());
        System.out.println(Arrays.toString(authException.getStackTrace()));
        // Eliminar la cookie del token JWT
        CookieUtil.removeJwtCookie(response);
        System.out.println("limpia cookies en entry point");
        //enviar al login si se existe una exepcion
        response.sendRedirect("/login");
    }
}
