package com.ipor.ticketsystem.model.dto.otros.WebSocket;

import com.ipor.ticketsystem.model.dto.AtencionTicketDTO;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public record RecepcionRecordWS(
        Long idTicket,
        String idFormateadoTicket,

        String fechaFormateadaTicket,
        String horaFormateadaTicket,
        String fechaFormateadaRecepcion,
        String horaFormateadaRecepcion,

        String descripcionTicket,
        String mensajeRecepcion,

        String nombreUsuarioTicket,
        String nombreUsuarioRecepcion,

        String nombreFaseTicket,
        String nombreClasificacionTicket,
        String nombreUrgenciaRecepcion,

        List<ArchivoAdjuntoDTO> listaArchivosAdjuntos
)
{
    public RecepcionRecordWS(AtencionTicketDTO atencionTicketDTO) {
        this(
                atencionTicketDTO.getTicket().getId(),
                atencionTicketDTO.getTicket().getIdFormateado(),

                atencionTicketDTO.getTicket().getFechaFormateada(),
                atencionTicketDTO.getTicket().getHoraFormateada(),
                atencionTicketDTO.getFechaFormateadaRecepcion(),
                atencionTicketDTO.getHoraFormateadaRecepcion(),

                atencionTicketDTO.getTicket().getDescripcion(),
                atencionTicketDTO.getMensaje(),


                atencionTicketDTO.getTicket().getUsuario().getNombre(),
                atencionTicketDTO.getUsuario().getNombre(),

                atencionTicketDTO.getTicket().getFaseTicket().getNombre(),

                atencionTicketDTO.getTicket().getClasificacionIncidencia().getNombre(),
                atencionTicketDTO.getClasificacionUrgencia().getNombre(),

                atencionTicketDTO.getTicket().getListaArchivosAdjuntos().stream()
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
