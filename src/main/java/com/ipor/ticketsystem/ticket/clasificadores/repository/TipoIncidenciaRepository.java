package com.ipor.ticketsystem.ticket.clasificadores.repository;

import com.ipor.ticketsystem.ticket.clasificadores.model.TipoIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoIncidenciaRepository extends JpaRepository<TipoIncidencia, Long> {
    List<TipoIncidencia> findByIsActiveTrue();
    List<TipoIncidencia> findAllByOrderByNombreAsc();
    List<TipoIncidencia> findBySubCategoriaIncidenciaIdAndIsActiveTrue(Long idSubCategoria);
    List<TipoIncidencia> findBySubCategoriaIncidenciaIdOrderByNombreAsc(Long idSubCategoria);
}
