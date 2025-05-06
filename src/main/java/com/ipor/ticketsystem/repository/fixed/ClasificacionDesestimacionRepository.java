package com.ipor.ticketsystem.repository.fixed;

import com.ipor.ticketsystem.model.fixed.ClasificacionDesestimacion;
import com.ipor.ticketsystem.model.fixed.ClasificacionIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClasificacionDesestimacionRepository extends JpaRepository<ClasificacionDesestimacion, Long> {
    List<ClasificacionDesestimacion> findByIsActiveTrue();
    List<ClasificacionDesestimacion> findAllByOrderByNombreAsc();

    ClasificacionDesestimacion findByNombre(String nombre);
}
