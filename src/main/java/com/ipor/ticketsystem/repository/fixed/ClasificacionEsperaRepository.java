package com.ipor.ticketsystem.repository.fixed;

import com.ipor.ticketsystem.model.fixed.ClasificacionAtencion;
import com.ipor.ticketsystem.model.fixed.ClasificacionEspera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClasificacionEsperaRepository extends JpaRepository<ClasificacionEspera, Long> {
    List<ClasificacionEspera> findByIsActiveTrue();
    List<ClasificacionEspera> findAllByOrderByNombreAsc();


    ClasificacionEspera findByNombre(String nombre);
}
