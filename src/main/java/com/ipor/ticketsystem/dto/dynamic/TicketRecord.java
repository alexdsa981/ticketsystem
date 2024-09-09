package com.ipor.ticketsystem.dto.dynamic;

import com.ipor.ticketsystem.model.dynamic.Ticket;

public record TicketRecord(
        Long id,
        String descripcion,
        Long usuarioId,
        Long clasificacionIncidenciaId,
        Long faseTicketId
) {
    public TicketRecord(Ticket ticket) {
        this(ticket.getId(), ticket.getDescripcion(), ticket.getUsuario().getId(), ticket.getClasificacionIncidencia().getId(), ticket.getFaseTicket().getId());
    }
}
