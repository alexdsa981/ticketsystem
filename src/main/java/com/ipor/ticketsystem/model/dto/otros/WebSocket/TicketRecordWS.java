package com.ipor.ticketsystem.model.dto.otros.WebSocket;

import com.ipor.ticketsystem.model.dto.DetalleTicketDTO;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjuntoEnvio;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjuntoEspera;
import com.ipor.ticketsystem.model.dynamic.DetalleEnEspera;
import com.ipor.ticketsystem.model.fixed.ClasificacionEspera;

import java.time.format.DateTimeFormatter;
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
        List<ArchivoAdjuntoDTO> listaArchivosAdjuntosEnvio,
        List<DetalleEsperaDTO> listaDetalleEspera
)
{
    public TicketRecordWS(DetalleTicketDTO detalleDTO) {
        this(
                detalleDTO.getTicket().getId(),
                detalleDTO.getTicket().getCodigoTicket(),
                detalleDTO.getFechaFormateadaTicket(),
                detalleDTO.getHoraFormateadaTicket(),
                detalleDTO.getTicket().getDescripcion(),
                detalleDTO.getTicket().getUsuario().getNombre(),
                detalleDTO.getTicket().getFaseTicket().getNombre(),
                detalleDTO.getTicket().getListaArchivosAdjuntos().stream()
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
            List<ArchivoAdjuntoDTO> listaArchivos
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
                            .map(ArchivoAdjuntoDTO::new)
                            .collect(Collectors.toList())
            );
        }
    }
}
