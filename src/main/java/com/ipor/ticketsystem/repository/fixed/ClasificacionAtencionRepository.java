package com.ipor.ticketsystem.repository.fixed;

import com.ipor.ticketsystem.model.fixed.ClasificacionAtencion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClasificacionAtencionRepository extends JpaRepository<ClasificacionAtencion, Long> {
    List<ClasificacionAtencion> findByIsActiveTrue();
    List<ClasificacionAtencion> findAllByOrderByNombreAsc();


    ClasificacionAtencion findByNombre(String nombre);
}
