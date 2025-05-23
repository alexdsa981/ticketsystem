package com.ipor.ticketsystem.repository.fixed;

import com.ipor.ticketsystem.model.fixed.AreaAtencion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AreaAtencionRepository extends JpaRepository<AreaAtencion, Long> {
    List<AreaAtencion> findAllByOrderByNombreAsc();
    AreaAtencion findByNombre(String nombre);
    List<AreaAtencion> findBySedeIdOrderByNombreAsc(Long id);
    List<AreaAtencion> findBySedeIdAndIsActiveTrueOrderByNombreAsc (Long id);
}
