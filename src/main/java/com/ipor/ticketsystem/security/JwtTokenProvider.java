package com.ipor.ticketsystem.security;

import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtTokenProvider {

    //metodo para crear un token por medio de la authenticacion
    public String generarToken(Authentication authentication) {
        String username = authentication.getName();
        Date tiempoActual = new Date();
        Date expiracionToken = new Date(tiempoActual.getTime() + ConstantesSeguridad.JWT_EXPIRATION_TOKEN);

        //linea para generar el token
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiracionToken)
                .signWith(SignatureAlgorithm.HS512, ConstantesSeguridad.JWT_FIRMA)
                .compact();
        System.out.println("token generado: " + token);
        return token;
    }

    //metodo para extraer un username a partir de un token
    public String obtenerUsernameDeJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(ConstantesSeguridad.JWT_FIRMA)
                .parseClaimsJws(token)
                .getBody();
        System.out.println("token de obtener usuario por token: " + token);
        return claims.getSubject();
    }

    public Boolean validarToken(String token){
        System.out.println("token antes de validacion: " + token);
        try {
            Jwts.parser().setSigningKey(ConstantesSeguridad.JWT_FIRMA).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {

            throw new AuthenticationCredentialsNotFoundException("El token ha expirado: " + token, ex);
        } catch (MalformedJwtException ex) {
            throw new AuthenticationCredentialsNotFoundException("Token JWT mal formado: " + token, ex);
        } catch (SignatureException ex) {
            throw new AuthenticationCredentialsNotFoundException("Fallo en la firma del token JWT: " + token, ex);
        } catch (IllegalArgumentException ex) {
            throw new AuthenticationCredentialsNotFoundException("El token JWT está vacío o es incorrecto: " + token, ex);
        }
    }

}
