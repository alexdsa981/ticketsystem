package com.ipor.ticketsystem.repository.fixed;

import com.ipor.ticketsystem.model.fixed.ClasificacionServicio;
import com.ipor.ticketsystem.model.fixed.ClasificacionUrgencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClasificacionServicioRepository extends JpaRepository<ClasificacionServicio, Long> {
    List<ClasificacionServicio> findByIsActiveTrue();
}
