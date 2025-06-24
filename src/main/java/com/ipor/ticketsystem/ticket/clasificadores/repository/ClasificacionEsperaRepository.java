package com.ipor.ticketsystem.ticket.clasificadores.repository;

import com.ipor.ticketsystem.ticket.clasificadores.model.ClasificacionEspera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClasificacionEsperaRepository extends JpaRepository<ClasificacionEspera, Long> {
    List<ClasificacionEspera> findByIsActiveTrue();
    List<ClasificacionEspera> findAllByOrderByNombreAsc();
    @Query("SELECT c FROM ClasificacionEspera c WHERE c.id <> 1")
    List<ClasificacionEspera> findAllExceptId1();
    @Query("SELECT c FROM ClasificacionEspera c WHERE c.isActive = true AND c.id <> 1")
    List<ClasificacionEspera> findByIsActiveTrueAndIdNot1();


    ClasificacionEspera findByNombre(String nombre);
}
