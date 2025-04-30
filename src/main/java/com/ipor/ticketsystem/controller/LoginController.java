package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.dto.UsuarioSpringDTO;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/app")
public class LoginController {

    private UsuarioRepository usuarioRepository;
    private RolUsuarioRepository rolUsuarioRepository;
    private UsuarioService usuarioService;

    @Autowired
    public LoginController(UsuarioService usuarioService, UsuarioRepository usuarioRepository, RolUsuarioRepository rolUsuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolUsuarioRepository = rolUsuarioRepository;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) throws IOException {
        return usuarioService.logearUsuarioAlSistema(username, password, response);
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
