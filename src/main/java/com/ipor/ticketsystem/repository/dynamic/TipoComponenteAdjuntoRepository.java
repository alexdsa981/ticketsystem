package com.ipor.ticketsystem.repository.dynamic;


import com.ipor.ticketsystem.model.dynamic.TipoComponenteAdjunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TipoComponenteAdjuntoRepository extends JpaRepository<TipoComponenteAdjunto, Long> {
    @Query("SELECT a FROM TipoComponenteAdjunto a WHERE a.ticket.id = :ticketId")
    List<TipoComponenteAdjunto> BuscarPorIdTicket(@Param("ticketId") Long ticketId);

}
