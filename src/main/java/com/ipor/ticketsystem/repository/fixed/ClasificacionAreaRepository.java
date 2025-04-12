package com.ipor.ticketsystem.repository.fixed;

import com.ipor.ticketsystem.model.fixed.ClasificacionArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClasificacionAreaRepository extends JpaRepository<ClasificacionArea, Long> {
    List<ClasificacionArea> findByIsActiveTrue();
    ClasificacionArea findByNombre(String nombre);
}
