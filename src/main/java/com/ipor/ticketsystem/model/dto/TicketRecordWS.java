package com.ipor.ticketsystem.model.dto;

import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public record TicketRecordWS(
        Long id,
        String fechaFormateada,
        String horaFormateada,
        String descripcion,
        String nombreUsuario,
        String nombreClasificacionIncidencia,
        String nombreFaseTicket,
        List<ArchivoAdjuntoDTO> listaArchivosAdjuntos
) {
    public TicketRecordWS(TicketDTO ticketDTO) {
        this(
                ticketDTO.getId(),
                ticketDTO.getFechaFormateada(),
                ticketDTO.getHoraFormateada(),
                ticketDTO.getDescripcion(),
                ticketDTO.getUsuario().getNombre(),
                ticketDTO.getClasificacionIncidencia().getNombre(),
                ticketDTO.getFaseTicket().getNombre(),
                ticketDTO.getListaArchivosAdjuntos().stream()
                        .map(ArchivoAdjuntoDTO::new) // Convierte cada ArchivoAdjunto a ArchivoAdjuntoDTO
                        .collect(Collectors.toList())
        );
    }

    // Clase interna para representar los detalles del archivo adjunto
    public static record ArchivoAdjuntoDTO(
            Long id,
            String nombre,
            String archivoBase64,
            String tipoContenido,
            String pesoContenido
    ) {
        public ArchivoAdjuntoDTO(ArchivoAdjunto adjunto) {
            this(
                    adjunto.getId(),
                    adjunto.getNombre(),
                    Base64.getEncoder().encodeToString(adjunto.getArchivo()),
                    adjunto.getTipoContenido(),
                    adjunto.getPesoEnMegabytes()
            );
        }
    }
}
