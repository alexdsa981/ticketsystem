package com.ipor.ticketsystem.repository.dynamic;

import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArchivoAdjuntoRepository extends JpaRepository<ArchivoAdjunto, Long> {
    @Query("SELECT a FROM ArchivoAdjunto a WHERE a.ticket.id = :ticketId")
    List<ArchivoAdjunto> BuscarPorIdTicket(@Param("ticketId") Long ticketId);

}
