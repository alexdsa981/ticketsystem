package com.ipor.ticketsystem.repository.dynamic;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TipoComponenteAdjunto extends JpaRepository<TipoComponenteAdjunto, Long> {
    @Query("SELECT a FROM TipoComponenteAdjunto a WHERE a.ticket.id = :ticketId")
    List<TipoComponenteAdjunto> BuscarPorIdTicket(@Param("ticketId") Long ticketId);

}
