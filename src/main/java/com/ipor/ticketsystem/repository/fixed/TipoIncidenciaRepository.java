package com.ipor.ticketsystem.repository.fixed;

import com.ipor.ticketsystem.model.fixed.TipoIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoIncidenciaRepository extends JpaRepository<TipoIncidencia, Long> {
    List<TipoIncidencia> findByIsActiveTrue();
    List<TipoIncidencia> findAllByOrderByNombreAsc();
    TipoIncidencia findByNombre(String nombre);
}
