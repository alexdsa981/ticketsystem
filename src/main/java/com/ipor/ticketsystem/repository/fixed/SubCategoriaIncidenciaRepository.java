package com.ipor.ticketsystem.repository.fixed;

import com.ipor.ticketsystem.model.fixed.CategoriaIncidencia;
import com.ipor.ticketsystem.model.fixed.SubCategoriaIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoriaIncidenciaRepository extends JpaRepository<SubCategoriaIncidencia, Long> {
    List<SubCategoriaIncidencia> findByIsActiveTrue();
    List<SubCategoriaIncidencia> findAllByOrderByNombreAsc();
    List<SubCategoriaIncidencia> findByCategoriaIncidenciaIdOrderByNombreAsc(Long idCategoria);
    List<SubCategoriaIncidencia> findByCategoriaIncidenciaIdAndIsActiveTrue(Long idCategoria);
}
