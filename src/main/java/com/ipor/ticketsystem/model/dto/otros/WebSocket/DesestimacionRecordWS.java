package com.ipor.ticketsystem.model.dto.otros.WebSocket;

import com.ipor.ticketsystem.model.dto.AtencionTicketDTO;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;
import com.ipor.ticketsystem.model.dynamic.TipoComponenteAdjunto;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public record DesestimacionRecordWS(
        Long idTicket,
        String idFormateadoTicket,

        String fechaFormateadaTicket,
        String horaFormateadaTicket,
        String fechaFormateadaDesestimacion,
        String horaFormateadaDesestimacion,

        String descripcionTicket,
        String descripcionDesestimacion,

        String nombreUsuarioTicket,
        String nombreUsuarioDesestimacion,

        String nombreFaseTicket,

        String nombreClasificacionDesestimacion,

        List<ArchivoAdjuntoDTO> listaArchivosAdjuntos
)
{
    public DesestimacionRecordWS(AtencionTicketDTO atencionTicketDTO) {
        this(
                atencionTicketDTO.getTicket().getId(),
                atencionTicketDTO.getTicket().getIdFormateado(),

                atencionTicketDTO.getTicket().getFechaFormateada(),
                atencionTicketDTO.getTicket().getHoraFormateada(),
                atencionTicketDTO.getFechaFormateadaDesestimacion(),
                atencionTicketDTO.getHoraFormateadaDesestimacion(),

                atencionTicketDTO.getTicket().getDescripcion(),
                atencionTicketDTO.getDescripcion(),//aqui maneja la desc desestimacion en el dto
                atencionTicketDTO.getTicket().getUsuario().getNombre(),
                atencionTicketDTO.getUsuario().getNombre(),
                atencionTicketDTO.getTicket().getFaseTicket().getNombre(),
                atencionTicketDTO.getClasificacionDesestimacion().getNombre(),

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
