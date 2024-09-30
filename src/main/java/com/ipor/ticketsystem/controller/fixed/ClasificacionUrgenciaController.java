package com.ipor.ticketsystem.controller.fixed;
import com.ipor.ticketsystem.dto.fixed.ClasificacionUrgenciaRecord;
import com.ipor.ticketsystem.model.fixed.ClasificacionUrgencia;
import com.ipor.ticketsystem.repository.fixed.ClasificacionUrgenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/clasificacion-urgencia")
public class ClasificacionUrgenciaController {

    private final ClasificacionUrgenciaRepository clasificacionUrgenciaRepository;

    @Autowired
    public ClasificacionUrgenciaController(ClasificacionUrgenciaRepository clasificacionUrgenciaRepository) {
        this.clasificacionUrgenciaRepository = clasificacionUrgenciaRepository;
    }

    // Obtener todas las clasificaciones de urgencia
    @GetMapping
    public ResponseEntity<List<ClasificacionUrgenciaRecord>> getAllClasificacionesUrgencia() {
        List<ClasificacionUrgenciaRecord> clasificaciones = clasificacionUrgenciaRepository.findAll()
                .stream()
                .map(ClasificacionUrgenciaRecord::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clasificaciones);
    }

    // Obtener una clasificación de urgencia por ID
    @GetMapping("/{id}")
    public ResponseEntity<ClasificacionUrgenciaRecord> getClasificacionUrgenciaById(@PathVariable Long id) {
        Optional<ClasificacionUrgencia> clasificacionUrgencia = clasificacionUrgenciaRepository.findById(id);
        return clasificacionUrgencia.map(ClasificacionUrgenciaRecord::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva clasificación de urgencia
    @PostMapping
    public ResponseEntity<ClasificacionUrgenciaRecord> createClasificacionUrgencia(@RequestBody ClasificacionUrgenciaRecord clasificacionUrgenciaRecord) {
        ClasificacionUrgencia clasificacionUrgencia = new ClasificacionUrgencia();
        clasificacionUrgencia.setNombre(clasificacionUrgenciaRecord.nombre());
        ClasificacionUrgencia savedClasificacion = clasificacionUrgenciaRepository.save(clasificacionUrgencia);
        ClasificacionUrgenciaRecord createdRecord = new ClasificacionUrgenciaRecord(savedClasificacion);
        return ResponseEntity.ok(createdRecord);
    }

    // Actualizar una clasificación de urgencia existente
    @PutMapping("/{id}")
    public ResponseEntity<ClasificacionUrgenciaRecord> updateClasificacionUrgencia(@PathVariable Long id, @RequestBody ClasificacionUrgenciaRecord clasificacionUrgenciaRecord) {
        if (!clasificacionUrgenciaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ClasificacionUrgencia clasificacionUrgencia = new ClasificacionUrgencia();
        clasificacionUrgencia.setId(id);
        clasificacionUrgencia.setNombre(clasificacionUrgenciaRecord.nombre());
        ClasificacionUrgencia savedClasificacion = clasificacionUrgenciaRepository.save(clasificacionUrgencia);
        ClasificacionUrgenciaRecord updatedRecord = new ClasificacionUrgenciaRecord(savedClasificacion);
        return ResponseEntity.ok(updatedRecord);
    }

    // Eliminar una clasificación de urgencia por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClasificacionUrgencia(@PathVariable Long id) {
        if (!clasificacionUrgenciaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clasificacionUrgenciaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
