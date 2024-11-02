package com.ipor.ticketsystem.repository.fixed;

import com.ipor.ticketsystem.model.fixed.ClasificacionUrgencia;
import com.ipor.ticketsystem.model.fixed.FaseTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaseTicketRepository extends JpaRepository<FaseTicket, Long> {
    FaseTicket findByNombre(String nombre);
}
