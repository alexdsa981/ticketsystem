package com.ipor.ticketsystem.ticket.clasificadores.repository;

import com.ipor.ticketsystem.ticket.clasificadores.model.ClasificacionUrgencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClasificacionUrgenciaRepository extends JpaRepository<ClasificacionUrgencia, Long> {
    List<ClasificacionUrgencia> findByIsActiveTrue();
    ClasificacionUrgencia findByNombre(String nombre);
}
