package com.ipor.ticketsystem.dto.fixed;

import com.ipor.ticketsystem.model.fixed.FaseTicket;

public record FaseTicketRecord(
        Long id,
        String nombre
) {
    public FaseTicketRecord(FaseTicket faseTicket) {
        this(faseTicket.getId(), faseTicket.getNombre());
    }
}
