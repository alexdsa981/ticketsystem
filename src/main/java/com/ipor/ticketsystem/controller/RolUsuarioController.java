package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.dto.fixed.RolUsuarioRecord;
import com.ipor.ticketsystem.model.fixed.RolUsuario;
import com.ipor.ticketsystem.repository.fixed.RolUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RolUsuarioController {

    @Autowired
    private RolUsuarioRepository rolUsuarioRepository;

    // Obtener todos los roles
    @GetMapping
    public List<RolUsuario> getAllRoles() {
        return rolUsuarioRepository.findAll();
    }

    // Obtener un rol por ID
    @GetMapping("/{id}")
    public ResponseEntity<RolUsuario> getRoleById(@PathVariable Long id) {
        Optional<RolUsuario> rolUsuario = rolUsuarioRepository.findById(id);
        return rolUsuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo rol
    @PostMapping
    public RolUsuario createRole(@RequestBody RolUsuarioRecord rolUsuarioRecord) {
        RolUsuario rolUsuario = new RolUsuario(rolUsuarioRecord);
        return rolUsuarioRepository.save(rolUsuario);
    }

    // Actualizar un rol existente
    @PutMapping("/{id}")
    public ResponseEntity<RolUsuario> updateRole(@PathVariable Long id, @RequestBody RolUsuario updatedRolUsuario) {
        if (!rolUsuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        updatedRolUsuario.setId(id);
        RolUsuario savedRolUsuario = rolUsuarioRepository.save(updatedRolUsuario);
        return ResponseEntity.ok(savedRolUsuario);
    }

    // Eliminar un rol por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        if (!rolUsuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        rolUsuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
