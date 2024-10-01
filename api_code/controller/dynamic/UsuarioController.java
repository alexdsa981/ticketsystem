package com.ipor.ticketsystem.api_code.controller.dynamic;

import com.ipor.ticketsystem.api_code.dto.dynamic.UsuarioRecord;
import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.repository.dynamic.UsuarioRepository;
import com.ipor.ticketsystem.repository.fixed.RolUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final RolUsuarioRepository rolUsuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioController(UsuarioRepository usuarioRepository, RolUsuarioRepository rolUsuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolUsuarioRepository = rolUsuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioRecord>> getAllUsuarios() {
        List<UsuarioRecord> usuarios = usuarioRepository.findAll()
                .stream()
                .map(UsuarioRecord::new)  // No incluimos el password en la respuesta
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    // Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioRecord> getUsuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.map(UsuarioRecord::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<UsuarioRecord> createUsuario(@RequestBody UsuarioRecord usuarioRecord) {
        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioRecord.username());
        usuario.setNombre(usuarioRecord.nombre());
        usuario.setRolUsuario(rolUsuarioRepository.findById(usuarioRecord.rolUsuarioId()).orElseThrow());
        usuario.encriptarPassword(usuarioRecord.password());  // Encripta la contraseña
        Usuario savedUsuario = usuarioRepository.save(usuario);
        UsuarioRecord createdRecord = new UsuarioRecord(savedUsuario);
        return ResponseEntity.ok(createdRecord);
    }

    // Actualizar un usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioRecord> updateUsuario(@PathVariable Long id, @RequestBody UsuarioRecord usuarioRecord) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        usuario.setUsername(usuarioRecord.username());
        usuario.setNombre(usuarioRecord.nombre());
        usuario.setRolUsuario(rolUsuarioRepository.findById(usuarioRecord.rolUsuarioId()).orElseThrow());
        usuario.encriptarPassword(usuarioRecord.password());  // Actualiza la contraseña encriptada
        Usuario savedUsuario = usuarioRepository.save(usuario);
        UsuarioRecord updatedRecord = new UsuarioRecord(savedUsuario);
        return ResponseEntity.ok(updatedRecord);
    }

    // Eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

