package com.ipor.ticketsystem.model.dto.otros.WebSocket;

import com.ipor.ticketsystem.model.dto.DetalleTicketDTO;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;

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
    public DesestimacionRecordWS(DetalleTicketDTO detalleDTO) {
        this(
                detalleDTO.getTicket().getId(),
                detalleDTO.getTicket().getCodigoTicket(),

                detalleDTO.getFechaFormateadaTicket(),
                detalleDTO.getHoraFormateadaTicket(),

                detalleDTO.getFechaFormateadaDesestimacion(),
                detalleDTO.getHoraFormateadaDesestimacion(),

                detalleDTO.getTicket().getDescripcion(),
                detalleDTO.getDesestimacion().getDescripcion(),

                detalleDTO.getTicket().getUsuario().getNombre(),
                detalleDTO.getDesestimacion().getUsuario().getNombre(),

                detalleDTO.getTicket().getFaseTicket().getNombre(),
                detalleDTO.getDesestimacion().getClasificacionDesestimacion().getNombre(),

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
