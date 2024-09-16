package com.ipor.ticketsystem.repository.dynamic;

import com.ipor.ticketsystem.model.dynamic.Recepcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecepcionRepository extends JpaRepository<Recepcion, Long> {

    @Query("SELECT COUNT(r) > 0 FROM Recepcion r WHERE r.ticket.id = :ticketId")
    boolean existsByTicketId(Long ticketId);
}
