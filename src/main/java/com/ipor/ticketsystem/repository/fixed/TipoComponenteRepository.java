package com.ipor.ticketsystem.repository.fixed;


import com.ipor.ticketsystem.model.fixed.TipoComponente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoComponenteRepository extends JpaRepository<TipoComponente, Long> {
    List<TipoComponente> findByIsActiveTrue();
    TipoComponente findByNombre(String nombre);
}
