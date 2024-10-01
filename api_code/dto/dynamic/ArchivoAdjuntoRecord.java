package com.ipor.ticketsystem.api_code.dto.dynamic;

import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;

public record ArchivoAdjuntoRecord(
        Long id,
        String nombre,
        byte[] archivo,
        String tipoContenido,
        Long ticketId
) {
    public ArchivoAdjuntoRecord(ArchivoAdjunto archivoAdjunto) {
        this(
                archivoAdjunto.getId(),
                archivoAdjunto.getNombre(),
                archivoAdjunto.getArchivo(),
                archivoAdjunto.getTipoContenido(),
                archivoAdjunto.getTicket().getId()
        );
    }
}
