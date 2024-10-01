package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.repository.dynamic.UsuarioRepository;
import com.ipor.ticketsystem.repository.fixed.RolUsuarioRepository;
import com.ipor.ticketsystem.security.ConstantesSeguridad;
import com.ipor.ticketsystem.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/app")
public class LoginController {

    private AuthenticationManager authenticationManager;
    private UsuarioRepository usuarioRepository;
    private RolUsuarioRepository rolUsuarioRepository;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository, RolUsuarioRepository rolUsuarioRepository, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.rolUsuarioRepository = rolUsuarioRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) throws IOException {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.generarToken(authentication);

            // Crear una cookie para almacenar el token JWT
            Cookie jwtCookie = new Cookie("JWT", token);
            jwtCookie.setHttpOnly(true); // Para evitar el acceso desde JavaScript
            jwtCookie.setMaxAge((int) (ConstantesSeguridad.JWT_EXPIRATION_TOKEN)/1000); //12horas
            jwtCookie.setPath("/"); // Hacer accesible la cookie en toda la aplicación
            response.addCookie(jwtCookie);

            // Redirigir a la página principal o donde desees
            response.sendRedirect("/inicio");

            return ResponseEntity.ok().build();
        } catch (BadCredentialsException e) {
            response.sendRedirect("/login");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            response.sendRedirect("/login");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) throws IOException {
        // Eliminar la cookie del token JWT
        Cookie jwtCookie = new Cookie("JWT", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(0); // Esto elimina la cookie
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

        // Redirigir al login u otra página
        response.sendRedirect("/login");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
