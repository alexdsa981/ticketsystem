package com.ipor.ticketsystem.core.websocket.dto;

import com.ipor.ticketsystem.ticket.dto.DetalleTicketDTO;
import com.ipor.ticketsystem.ticket.adjuntos.ArchivoAdjuntoAtencion;
import com.ipor.ticketsystem.ticket.adjuntos.ArchivoAdjuntoEnvio;
import com.ipor.ticketsystem.ticket.adjuntos.ArchivoAdjuntoEspera;
import com.ipor.ticketsystem.ticket.DetalleEnEspera;
import com.ipor.ticketsystem.ticket.clasificadores.model.ClasificacionEspera;

import java.time.format.DateTimeFormatter;
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
        String nombreSubCatIncidencia,
        String nombreTipoIncidencia,
        String nombreUrgencia,
        String nombreClasificacionAtencion,
        String nombreAreaAtencion,
        String nombreSedeAtencion,

        List<ArchivoAdjuntoDTO> listaArchivosAdjuntosEnvio,
        List<ArchivoAdjuntoDTO> listaArchivosAdjuntosAtencion,

        List<DetalleEsperaDTO> listaDetalleEspera
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
                detalleDTO.getFechaFormateadaAtencion(),
                detalleDTO.getHoraFormateadaAtencion(),

                detalleDTO.getTicket().getDescripcion(),
                detalleDTO.getRecepcion().getMensaje(),
                detalleDTO.getAtencion().getDescripcion(),

                detalleDTO.getTicket().getUsuario().getNombre(),
                detalleDTO.getRecepcion().getUsuario().getNombre(),
                detalleDTO.getAtencion().getUsuario().getNombre(),

                detalleDTO.getTicket().getFaseTicket().getNombre(),
                detalleDTO.getAtencion().getTipoIncidencia().getSubCategoriaIncidencia().getNombre(),
                detalleDTO.getAtencion().getTipoIncidencia().getNombre(),
                detalleDTO.getAtencion().getClasificacionUrgencia().getNombre(),
                detalleDTO.getAtencion().getClasificacionAtencion().getNombre(),
                detalleDTO.getAtencion().getAreaAtencion().getNombre(),
                detalleDTO.getAtencion().getAreaAtencion().getSede().getNombre(),

                detalleDTO.getTicket().getListaArchivosAdjuntos().stream()
                        .map(ArchivoAdjuntoDTO::new)
                        .collect(Collectors.toList()),
                detalleDTO.getAtencion().getListaArchivosAdjuntos().stream()
                        .map(ArchivoAdjuntoDTO::new)
                        .collect(Collectors.toList()),
                detalleDTO.getDetalleEnEspera().stream()
                        .map(DetalleEsperaDTO::new)
                        .collect(Collectors.toList())
        );
    }

    public static record ArchivoAdjuntoDTO(
            Long id,
            String nombre,
            String archivoBase64,
            String tipoContenido,
            String pesoContenido
    ) {
        public ArchivoAdjuntoDTO(ArchivoAdjuntoEnvio adjunto) {
            this(
                    adjunto.getId(),
                    adjunto.getNombre(),
                    Base64.getEncoder().encodeToString(adjunto.getArchivo()),
                    adjunto.getTipoContenido(),
                    adjunto.getPesoEnMegabytes()
            );
        }

        public ArchivoAdjuntoDTO(ArchivoAdjuntoAtencion adjunto) {
            this(
                    adjunto.getId(),
                    adjunto.getNombre(),
                    Base64.getEncoder().encodeToString(adjunto.getArchivo()),
                    adjunto.getTipoContenido(),
                    adjunto.getPesoEnMegabytes()
            );
        }

        public ArchivoAdjuntoDTO(ArchivoAdjuntoEspera adjunto) {
            this(
                    adjunto.getId(),
                    adjunto.getNombre(),
                    Base64.getEncoder().encodeToString(adjunto.getArchivo()),
                    adjunto.getTipoContenido(),
                    adjunto.getPesoEnMegabytes()
            );
        }
    }

    public static record DetalleEsperaDTO(
            ClasificacionEspera clasificacion,
            String fecha,
            String hora,
            String fechaFin,
            String horaFin,
            String descripcion,
            List<TicketRecordWS.ArchivoAdjuntoDTO> listaArchivos
    ) {
        public DetalleEsperaDTO(DetalleEnEspera espera) {
            this(
                    espera.getClasificacionEspera(),
                    espera.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    espera.getHoraInicio().format(DateTimeFormatter.ofPattern("HH:mm")),
                    espera.getFechaFin() != null
                            ? espera.getFechaFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            : "En curso",
                    espera.getHoraFin() != null
                            ? espera.getHoraFin().format(DateTimeFormatter.ofPattern("HH:mm"))
                            : "En curso",

                    espera.getDescripcion(),
                    espera.getListaArchivosAdjuntos().stream()
                            .map(TicketRecordWS.ArchivoAdjuntoDTO::new)
                            .collect(Collectors.toList())
            );
        }
    }
}
