package com.ipor.ticketsystem.ticket.clasificadores.repository;

import com.ipor.ticketsystem.ticket.clasificadores.model.SubCategoriaIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoriaIncidenciaRepository extends JpaRepository<SubCategoriaIncidencia, Long> {
    List<SubCategoriaIncidencia> findByIsActiveTrue();
    List<SubCategoriaIncidencia> findAllByOrderByNombreAsc();
    List<SubCategoriaIncidencia> findByCategoriaIncidenciaIdOrderByNombreAsc(Long idCategoria);
    List<SubCategoriaIncidencia> findByCategoriaIncidenciaIdAndIsActiveTrue(Long idCategoria);
}
