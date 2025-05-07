package com.ipor.ticketsystem.repository.fixed;

import com.ipor.ticketsystem.model.fixed.CategoriaIncidencia;
import com.ipor.ticketsystem.model.fixed.TipoIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaIncidenciaRepository extends JpaRepository<CategoriaIncidencia, Long> {
    List<CategoriaIncidencia> findByIsActiveTrue();
    List<CategoriaIncidencia> findAllByOrderByNombreAsc();
    CategoriaIncidencia findByNombre(String nombre);
}
