package com.ipor.ticketsystem.model.dto.otros.WebSocket;

import com.ipor.ticketsystem.model.dto.AtencionTicketDTO;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;
import com.ipor.ticketsystem.model.dynamic.TipoComponenteAdjunto;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public record AtencionRecordWS(
        Long idTicket,
        String idFormateadoTicket,

        String fechaFormateadaTicket,
        String horaFormateadaTicket,
        String fechaFormateadaRecepcion,
        String horaFormateadaRecepcion,
        String fechaFormateadaAtencion,
        String horaFormateadaAtencion,


        String descripcionTicket,
        String mensajeRecepcion,
        String descripcionAtencion,

        String nombreUsuarioTicket,
        String nombreUsuarioRecepcion,
        String nombreUsuarioAtencion,

        String nombreFaseTicket,
        String nombreClasificacionTicket,
        String nombreUrgenciaRecepcion,
        String nombreClasificacionAtencion,

        List<ArchivoAdjuntoDTO> listaArchivosAdjuntos,
        List<TipoComponenteAdjuntoDTO> listaComponentesAdjuntos
)
{
    public AtencionRecordWS(AtencionTicketDTO atencionTicketDTO) {
        this(
                atencionTicketDTO.getTicket().getId(),
                atencionTicketDTO.getTicket().getIdFormateado(),

                atencionTicketDTO.getTicket().getFechaFormateada(),
                atencionTicketDTO.getTicket().getHoraFormateada(),
                atencionTicketDTO.getFechaFormateadaRecepcion(),
                atencionTicketDTO.getHoraFormateadaRecepcion(),
                atencionTicketDTO.getFechaFormateadaServicio(),
                atencionTicketDTO.getHoraFormateadaServicio(),


                atencionTicketDTO.getTicket().getDescripcion(),
                atencionTicketDTO.getRecepcionHistorial().getMensaje(),
                atencionTicketDTO.getServicioHistorial().getDescripcion(),

                atencionTicketDTO.getTicket().getUsuario().getNombre(),
                atencionTicketDTO.getRecepcionHistorial().getUsuario().getNombre(),
                atencionTicketDTO.getServicioHistorial().getUsuario().getNombre(),

                atencionTicketDTO.getTicket().getFaseTicket().getNombre(),

                atencionTicketDTO.getTicket().getClasificacionIncidencia().getNombre(),
                atencionTicketDTO.getRecepcionHistorial().getClasificacionUrgencia().getNombre(),
                atencionTicketDTO.getServicioHistorial().getClasificacionServicio().getNombre(),

                atencionTicketDTO.getTicket().getListaArchivosAdjuntos().stream()
                        .map(ArchivoAdjuntoDTO::new) // Convierte cada ArchivoAdjunto a ArchivoAdjuntoDTO
                        .collect(Collectors.toList()),
                atencionTicketDTO.getTicket().getListaComponentesAdjuntos().stream()
                        .map(TipoComponenteAdjuntoDTO::new) //Convierte TipoComponenteAdjunto a ComponenteAdjuntoDTO
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


    // Clase interna para representar los detalles del archivo adjunto
    public static record TipoComponenteAdjuntoDTO(
            Long id,
            String nombre,
            Integer cantidad,
            Boolean aprobado
    ) {
        public TipoComponenteAdjuntoDTO(TipoComponenteAdjunto adjunto) {
            this(
                    adjunto.getId(),
                    adjunto.getTipoComponente().getNombre(),
                    adjunto.getCantidad(),
                    adjunto.getAprobado()
            );
        }
    }
}
