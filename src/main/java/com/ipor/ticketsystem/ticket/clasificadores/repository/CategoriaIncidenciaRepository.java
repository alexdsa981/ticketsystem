package com.ipor.ticketsystem.ticket.clasificadores.repository;

import com.ipor.ticketsystem.ticket.clasificadores.model.CategoriaIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaIncidenciaRepository extends JpaRepository<CategoriaIncidencia, Long> {
    List<CategoriaIncidencia> findByIsActiveTrue();
    List<CategoriaIncidencia> findAllByOrderByNombreAsc();
    CategoriaIncidencia findByNombre(String nombre);
}
