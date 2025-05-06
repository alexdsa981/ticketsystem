package com.ipor.ticketsystem.model.dto.otros.WebSocket;

import com.ipor.ticketsystem.model.dto.DetalleTicketDTO;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;

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
        String nombreClasificacionArea,

        List<ArchivoAdjuntoDTO> listaArchivosAdjuntos

)
{
    public AtencionRecordWS(DetalleTicketDTO detalleDTO) {
        this(
                detalleDTO.getTicket().getId(),
                detalleDTO.getTicket().getCodigoTicket(),

                detalleDTO.getFechaFormateadaTicket(),
                detalleDTO.getHoraFormateadaTicket(),
                detalleDTO.getFechaFormateadaRecepcion(),
                detalleDTO.getHoraFormateadaRecepcion(),
                detalleDTO.getFechaFormateadaServicio(),
                detalleDTO.getHoraFormateadaServicio(),


                detalleDTO.getTicket().getDescripcion(),
                detalleDTO.getRecepcion().getMensaje(),
                detalleDTO.getServicio().getDescripcion(),

                detalleDTO.getTicket().getUsuario().getNombre(),
                detalleDTO.getRecepcion().getUsuario().getNombre(),
                detalleDTO.getServicio().getUsuario().getNombre(),

                detalleDTO.getTicket().getFaseTicket().getNombre(),

                detalleDTO.getTicket().getClasificacionIncidencia().getNombre(),
                detalleDTO.getRecepcion().getClasificacionUrgencia().getNombre(),
                detalleDTO.getServicio().getClasificacionServicio().getNombre(),
                detalleDTO.getTicket().getClasificacionArea().getNombre(),

                detalleDTO.getTicket().getListaArchivosAdjuntos().stream()
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
