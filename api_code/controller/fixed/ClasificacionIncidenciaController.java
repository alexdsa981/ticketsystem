package com.ipor.ticketsystem.api_code.controller.fixed;

import com.ipor.ticketsystem.api_code.dto.fixed.ClasificacionIncidenciaRecord;
import com.ipor.ticketsystem.model.fixed.ClasificacionIncidencia;
import com.ipor.ticketsystem.repository.fixed.ClasificacionIncidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/clasificacion-incidencia")
public class ClasificacionIncidenciaController {

    private final ClasificacionIncidenciaRepository clasificacionIncidenciaRepository;

    @Autowired
    public ClasificacionIncidenciaController(ClasificacionIncidenciaRepository clasificacionIncidenciaRepository) {
        this.clasificacionIncidenciaRepository = clasificacionIncidenciaRepository;
    }

    // Obtener todas las clasificaciones
    @GetMapping
    public ResponseEntity<List<ClasificacionIncidenciaRecord>> getAllClasificaciones() {
        List<ClasificacionIncidenciaRecord> clasificaciones = clasificacionIncidenciaRepository.findAll()
                .stream()
                .map(ClasificacionIncidenciaRecord::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clasificaciones);
    }

    // Obtener una clasificación por ID
    @GetMapping("/{id}")
    public ResponseEntity<ClasificacionIncidenciaRecord> getClasificacionById(@PathVariable Long id) {
        Optional<ClasificacionIncidencia> clasificacionIncidencia = clasificacionIncidenciaRepository.findById(id);
        return clasificacionIncidencia.map(ClasificacionIncidenciaRecord::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva clasificación
    @PostMapping
    public ResponseEntity<ClasificacionIncidenciaRecord> createClasificacion(@RequestBody ClasificacionIncidenciaRecord clasificacionIncidenciaRecord) {
        ClasificacionIncidencia clasificacionIncidencia = new ClasificacionIncidencia();
        clasificacionIncidencia.setNombre(clasificacionIncidenciaRecord.nombre());
        ClasificacionIncidencia savedClasificacion = clasificacionIncidenciaRepository.save(clasificacionIncidencia);
        ClasificacionIncidenciaRecord createdRecord = new ClasificacionIncidenciaRecord(savedClasificacion);
        return ResponseEntity.ok(createdRecord);
    }

    // Actualizar una clasificación existente
    @PutMapping("/{id}")
    public ResponseEntity<ClasificacionIncidenciaRecord> updateClasificacion(@PathVariable Long id, @RequestBody ClasificacionIncidenciaRecord clasificacionIncidenciaRecord) {
        if (!clasificacionIncidenciaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ClasificacionIncidencia clasificacionIncidencia = new ClasificacionIncidencia();
        clasificacionIncidencia.setId(id);
        clasificacionIncidencia.setNombre(clasificacionIncidenciaRecord.nombre());
        ClasificacionIncidencia savedClasificacion = clasificacionIncidenciaRepository.save(clasificacionIncidencia);
        ClasificacionIncidenciaRecord updatedRecord = new ClasificacionIncidenciaRecord(savedClasificacion);
        return ResponseEntity.ok(updatedRecord);
    }

    // Eliminar una clasificación por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClasificacion(@PathVariable Long id) {
        if (!clasificacionIncidenciaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clasificacionIncidenciaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
