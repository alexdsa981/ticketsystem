package com.ipor.ticketsystem.ticket.clasificadores.repository;

import com.ipor.ticketsystem.ticket.clasificadores.model.Sede;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SedeRepository extends JpaRepository<Sede, Long> {
    List<Sede> findByIsActiveTrueOrderByNombreAsc();
    List<Sede> findAllByOrderByNombreAsc();
    Sede findByNombre(String nombre);
}
