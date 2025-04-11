package com.ipor.ticketsystem.model.dto.otros.WebSocket;

import com.ipor.ticketsystem.model.dto.TicketDTO;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public record TicketRecordWS(
        Long id,
        String idFormateado,
        String fechaFormateada,
        String horaFormateada,
        String descripcion,
        String nombreUsuario,
        String nombreFaseTicket,
        List<ArchivoAdjuntoDTO> listaArchivosAdjuntos
)
{
    public TicketRecordWS(TicketDTO ticketDTO) {
        this(
                ticketDTO.getId(),
                ticketDTO.getIdFormateado(),
                ticketDTO.getFechaFormateada(),
                ticketDTO.getHoraFormateada(),
                ticketDTO.getDescripcion(),
                ticketDTO.getUsuario().getNombre(),
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
