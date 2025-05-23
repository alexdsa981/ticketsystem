package com.ipor.ticketsystem.repository.fixed;

import com.ipor.ticketsystem.model.fixed.AreaAtencion;
import com.ipor.ticketsystem.model.fixed.Sede;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SedeRepository extends JpaRepository<Sede, Long> {
    List<Sede> findByIsActiveTrueOrderByNombreAsc();
    List<Sede> findAllByOrderByNombreAsc();
    Sede findByNombre(String nombre);
}
