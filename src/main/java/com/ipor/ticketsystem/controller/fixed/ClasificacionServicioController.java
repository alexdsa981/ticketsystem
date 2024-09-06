package com.ipor.ticketsystem.controller.fixed;

import com.ipor.ticketsystem.dto.fixed.ClasificacionServicioRecord;
import com.ipor.ticketsystem.model.fixed.ClasificacionServicio;
import com.ipor.ticketsystem.repository.fixed.ClasificacionServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clasificaciones-servicio")
public class ClasificacionServicioController {

    private final ClasificacionServicioRepository clasificacionServicioRepository;

    @Autowired
    public ClasificacionServicioController(ClasificacionServicioRepository clasificacionServicioRepository) {
        this.clasificacionServicioRepository = clasificacionServicioRepository;
    }

    // Obtener todas las clasificaciones de servicio
    @GetMapping
    public ResponseEntity<List<ClasificacionServicioRecord>> getAllClasificacionesServicio() {
        List<ClasificacionServicioRecord> clasificaciones = clasificacionServicioRepository.findAll()
                .stream()
                .map(ClasificacionServicioRecord::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clasificaciones);
    }

    // Obtener una clasificación de servicio por ID
    @GetMapping("/{id}")
    public ResponseEntity<ClasificacionServicioRecord> getClasificacionServicioById(@PathVariable Long id) {
        Optional<ClasificacionServicio> clasificacionServicio = clasificacionServicioRepository.findById(id);
        return clasificacionServicio.map(ClasificacionServicioRecord::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva clasificación de servicio
    @PostMapping
    public ResponseEntity<ClasificacionServicioRecord> createClasificacionServicio(@RequestBody ClasificacionServicioRecord clasificacionServicioRecord) {
        ClasificacionServicio clasificacionServicio = new ClasificacionServicio();
        clasificacionServicio.setNombre(clasificacionServicioRecord.nombre());
        ClasificacionServicio savedClasificacion = clasificacionServicioRepository.save(clasificacionServicio);
        ClasificacionServicioRecord createdRecord = new ClasificacionServicioRecord(savedClasificacion);
        return ResponseEntity.ok(createdRecord);
    }

    // Actualizar una clasificación de servicio existente
    @PutMapping("/{id}")
    public ResponseEntity<ClasificacionServicioRecord> updateClasificacionServicio(@PathVariable Long id, @RequestBody ClasificacionServicioRecord clasificacionServicioRecord) {
        if (!clasificacionServicioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ClasificacionServicio clasificacionServicio = new ClasificacionServicio();
        clasificacionServicio.setId(id);
        clasificacionServicio.setNombre(clasificacionServicioRecord.nombre());
        ClasificacionServicio savedClasificacion = clasificacionServicioRepository.save(clasificacionServicio);
        ClasificacionServicioRecord updatedRecord = new ClasificacionServicioRecord(savedClasificacion);
        return ResponseEntity.ok(updatedRecord);
    }

    // Eliminar una clasificación de servicio por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClasificacionServicio(@PathVariable Long id) {
        if (!clasificacionServicioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clasificacionServicioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
