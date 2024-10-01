package com.ipor.ticketsystem.api_code.dto.dynamic;

import com.ipor.ticketsystem.model.dynamic.Recepcion;

public record RecepcionRecord(
        Long id,
        String mensaje,
        Long usuarioId,
        Long ticketId,
        Long clasificacionUrgenciaId
) {
    public RecepcionRecord(Recepcion recepcion) {
        this(recepcion.getId(), recepcion.getMensaje(), recepcion.getUsuario().getId(), recepcion.getTicket().getId(), recepcion.getClasificacionUrgencia().getId());
    }
}
