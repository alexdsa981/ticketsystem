package com.ipor.ticketsystem.api_code.dto.dynamic;

import com.ipor.ticketsystem.model.dynamic.Servicio;

public record ServicioRecord(
        Long id,
        String descripcion,
        Long usuarioId,
        Long ticketId,
        Long clasificacionAtencionId
) {
    public ServicioRecord(Servicio servicio) {
        this(servicio.getId(), servicio.getDescripcion(), servicio.getUsuario().getId(), servicio.getTicket().getId(), servicio.getClasificacionServicio().getId());
    }
}
