package com.ipor.ticketsystem.controller.fixed;

import com.ipor.ticketsystem.dto.fixed.RolUsuarioRecord;
import com.ipor.ticketsystem.model.fixed.RolUsuario;
import com.ipor.ticketsystem.repository.fixed.RolUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
public class RolUsuarioController {

    private final RolUsuarioRepository rolUsuarioRepository;

    @Autowired
    public RolUsuarioController(RolUsuarioRepository rolUsuarioRepository) {
        this.rolUsuarioRepository = rolUsuarioRepository;
    }

    // Obtener todos los roles
    @GetMapping
    public ResponseEntity<List<RolUsuarioRecord>> getAllRoles() {
        List<RolUsuarioRecord> roles = rolUsuarioRepository.findAll()
                .stream()
                .map(RolUsuarioRecord::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }


    // Obtener un rol por ID
    @GetMapping("/{id}")
    public ResponseEntity<RolUsuarioRecord> getRoleById(@PathVariable Long id) {
        Optional<RolUsuario> rolUsuario = rolUsuarioRepository.findById(id);
        return rolUsuario.map(RolUsuarioRecord::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo rol
    @PostMapping
    public ResponseEntity<RolUsuarioRecord> createRole(@RequestBody RolUsuarioRecord rolUsuarioRecord) {
        RolUsuario rolUsuario = new RolUsuario();
        rolUsuario.setNombre(rolUsuarioRecord.nombre());
        RolUsuario savedRolUsuario = rolUsuarioRepository.save(rolUsuario);
        RolUsuarioRecord createdRecord = new RolUsuarioRecord(savedRolUsuario);
        return ResponseEntity.ok(createdRecord);
    }

    // Actualizar un rol existente
    @PutMapping("/{id}")
    public ResponseEntity<RolUsuarioRecord> updateRole(@PathVariable Long id, @RequestBody RolUsuarioRecord rolUsuarioRecord) {
        if (!rolUsuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        RolUsuario rolUsuario = new RolUsuario();
        rolUsuario.setId(id);
        rolUsuario.setNombre(rolUsuarioRecord.nombre());
        RolUsuario savedRolUsuario = rolUsuarioRepository.save(rolUsuario);
        RolUsuarioRecord updatedRecord = new RolUsuarioRecord(savedRolUsuario);
        return ResponseEntity.ok(updatedRecord);
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

