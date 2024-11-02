package com.ipor.ticketsystem.repository.fixed;

import com.ipor.ticketsystem.model.fixed.ClasificacionUrgencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClasificacionUrgenciaRepository extends JpaRepository<ClasificacionUrgencia, Long> {
    List<ClasificacionUrgencia> findByIsActiveTrue();
    ClasificacionUrgencia findByNombre(String nombre);
}
