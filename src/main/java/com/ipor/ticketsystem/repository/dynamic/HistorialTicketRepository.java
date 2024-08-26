package com.ipor.ticketsystem.repository.dynamic;

import com.ipor.ticketsystem.model.dynamic.HistorialTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialTicketRepository extends JpaRepository<HistorialTicket, Long> {
}
