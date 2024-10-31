package com.ipor.ticketsystem.repository.fixed;

import com.ipor.ticketsystem.model.fixed.ClasificacionIncidencia;
import com.ipor.ticketsystem.model.fixed.ClasificacionServicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClasificacionIncidenciaRepository extends JpaRepository<ClasificacionIncidencia, Long> {
    List<ClasificacionIncidencia> findByIsActiveTrue();
}
