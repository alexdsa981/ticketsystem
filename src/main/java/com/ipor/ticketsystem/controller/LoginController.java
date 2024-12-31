package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.repository.dynamic.UsuarioRepository;
import com.ipor.ticketsystem.repository.fixed.RolUsuarioRepository;
import com.ipor.ticketsystem.security.ConstantesSeguridad;
import com.ipor.ticketsystem.security.JwtTokenProvider;
import com.ipor.ticketsystem.service.UsuarioService;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/app")
public class LoginController {

    private AuthenticationManager authenticationManager;
    private UsuarioRepository usuarioRepository;
    private RolUsuarioRepository rolUsuarioRepository;
    private JwtTokenProvider jwtTokenProvider;
    private UsuarioService usuarioService;

    @Autowired
    public LoginController(UsuarioService usuarioService, AuthenticationManager authenticationManager, UsuarioRepository usuarioRepository, RolUsuarioRepository rolUsuarioRepository, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.rolUsuarioRepository = rolUsuarioRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) throws IOException {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            // Usuario no existe
            response.sendRedirect("/login?error=badCredentials&username=" + username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else if (!usuarioOpt.get().getIsActive()) {
            // Usuario desactivado
            response.sendRedirect("/login?error=inactive&username=" + username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.generarToken(authentication);

            Cookie jwtCookie = new Cookie("JWT", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setMaxAge((int) (ConstantesSeguridad.JWT_EXPIRATION_TOKEN) / 1000);
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);


            if (!usuarioOpt.get().getChangedPass()) {
                // Usuario debe cambiar su contraseña
                response.sendRedirect("/inicio?changePassword");
                return ResponseEntity.ok().build();
            }


            response.sendRedirect("/inicio");
            return ResponseEntity.ok().build();
        } catch (BadCredentialsException e) {
            // Credenciales incorrectas
            response.sendRedirect("/login?error=badCredentials&username=" + username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            // Otro error
            response.sendRedirect("/login?error=unknown&username=" + username);
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

    @PostMapping("/cambiar-password")
    public ResponseEntity<Void> cambiarPassword(@RequestParam String newPassword) {
        Long iDUsuarioLogeado = usuarioService.getIDdeUsuarioLogeado();
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(iDUsuarioLogeado);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.encriptarPassword(newPassword);
            usuario.setChangedPass(true); // Actualiza el estado
            usuarioRepository.save(usuario); // Guarda los cambios en la BD
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


}
