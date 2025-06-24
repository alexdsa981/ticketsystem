package com.ipor.ticketsystem.ticket.clasificadores.repository;

import com.ipor.ticketsystem.ticket.clasificadores.model.ClasificacionAtencion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClasificacionAtencionRepository extends JpaRepository<ClasificacionAtencion, Long> {
    List<ClasificacionAtencion> findByIsActiveTrue();
    List<ClasificacionAtencion> findAllByOrderByNombreAsc();


    ClasificacionAtencion findByNombre(String nombre);
}
