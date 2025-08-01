package com.ipor.ticketsystem.ticket.adjuntos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArchivoAdjuntoEnvioRepository extends JpaRepository<ArchivoAdjuntoEnvio, Long> {
    @Query("SELECT a FROM ArchivoAdjuntoEnvio a WHERE a.ticket.id = :ticketId")
    List<ArchivoAdjuntoEnvio> BuscarPorIdTicket(@Param("ticketId") Long ticketId);

}
