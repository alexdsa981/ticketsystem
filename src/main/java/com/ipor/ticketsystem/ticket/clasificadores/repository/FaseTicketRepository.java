package com.ipor.ticketsystem.ticket.clasificadores.repository;

import com.ipor.ticketsystem.ticket.clasificadores.model.FaseTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaseTicketRepository extends JpaRepository<FaseTicket, Long> {
    FaseTicket findByNombre(String nombre);
}
