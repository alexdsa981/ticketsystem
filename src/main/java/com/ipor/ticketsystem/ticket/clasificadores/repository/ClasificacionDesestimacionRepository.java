package com.ipor.ticketsystem.ticket.clasificadores.repository;

import com.ipor.ticketsystem.ticket.clasificadores.model.ClasificacionDesestimacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClasificacionDesestimacionRepository extends JpaRepository<ClasificacionDesestimacion, Long> {
    List<ClasificacionDesestimacion> findByIsActiveTrue();
    List<ClasificacionDesestimacion> findAllByOrderByNombreAsc();

    ClasificacionDesestimacion findByNombre(String nombre);
}
