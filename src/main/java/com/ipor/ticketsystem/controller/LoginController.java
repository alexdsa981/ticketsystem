package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.dto.LoginRequest;
import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.repository.dynamic.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(loginRequest.getUsername());
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.verificarPassword(loginRequest.getPassword(), usuario.getPassword())) {
                return ResponseEntity.ok("Login exitoso");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }
}
