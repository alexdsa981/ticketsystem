package com.ipor.ticketsystem.dashboard.admin.exportar;

import com.ipor.ticketsystem.ticket.DetalleEnEspera;
import com.ipor.ticketsystem.ticket.dto.DetalleTicketDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketExportDTO {

    private Long idTicket;
    private String idFormateadoTicket;

    private String fechaFormateadaTicket;
    private String horaFormateadaTicket;
    private String fechaFormateadaRecepcion;
    private String horaFormateadaRecepcion;
    private String fechaFormateadaAtencion;
    private String horaFormateadaAtencion;

    private String descripcionTicket;
    private String mensajeRecepcion;
    private String descripcionAtencion;

    private String nombreUsuarioTicket;
    private String nombreUsuarioRecepcion;
    private String nombreUsuarioAtencion;

    private String nombreFaseTicket;
    private String nombreCategoriaIncidencia;
    private String nombreSubCatIncidencia;
    private String nombreTipoIncidencia;
    private String nombreUrgencia;
    private String nombreClasificacionAtencion;
    private String nombreAreaAtencion;
    private String nombreSedeAtencion;

    // Espera 1
    private String usuarioEspera1;
    private String clasificacionEspera1;
    private String descripcionEspera1;
    private String fechaInicioEspera1;
    private String horaInicioEspera1;
    private String fechaFinEspera1;
    private String horaFinEspera1;

    // Espera 2
    private String usuarioEspera2;
    private String clasificacionEspera2;
    private String descripcionEspera2;
    private String fechaInicioEspera2;
    private String horaInicioEspera2;
    private String fechaFinEspera2;
    private String horaFinEspera2;

    public static TicketExportDTO convertirADTOExportacion(DetalleTicketDTO dto) {
        TicketExportDTO exportDTO = new TicketExportDTO();

        exportDTO.setIdTicket(dto.getTicket().getId());
        exportDTO.setIdFormateadoTicket(dto.getTicket().getCodigoTicket());

        exportDTO.setFechaFormateadaTicket(dto.getFechaFormateadaTicket());
        exportDTO.setHoraFormateadaTicket(dto.getHoraFormateadaTicket());
        exportDTO.setFechaFormateadaRecepcion(dto.getFechaFormateadaRecepcion());
        exportDTO.setHoraFormateadaRecepcion(dto.getHoraFormateadaRecepcion());
        exportDTO.setFechaFormateadaAtencion(dto.getFechaFormateadaAtencion());
        exportDTO.setHoraFormateadaAtencion(dto.getHoraFormateadaAtencion());

        exportDTO.setDescripcionTicket(dto.getTicket().getDescripcion());
        exportDTO.setMensajeRecepcion(dto.getRecepcion() != null ? dto.getRecepcion().getMensaje() : "");
        exportDTO.setDescripcionAtencion(dto.getAtencion() != null ? dto.getAtencion().getDescripcion() : "");

        exportDTO.setNombreUsuarioTicket(dto.getTicket().getUsuario().getNombre());
        exportDTO.setNombreUsuarioRecepcion(dto.getRecepcion() != null ? dto.getRecepcion().getUsuario().getNombre() : "");
        exportDTO.setNombreUsuarioAtencion(dto.getAtencion() != null ? dto.getAtencion().getUsuario().getNombre() : "");

        exportDTO.setNombreFaseTicket(dto.getTicket().getFaseTicket().getNombre());
        exportDTO.setNombreCategoriaIncidencia(dto.getAtencion() != null ?
                dto.getAtencion().getTipoIncidencia().getSubCategoriaIncidencia().getCategoriaIncidencia().getNombre() : "");
        exportDTO.setNombreSubCatIncidencia(dto.getAtencion() != null ?
                dto.getAtencion().getTipoIncidencia().getSubCategoriaIncidencia().getNombre() : "");
        exportDTO.setNombreTipoIncidencia(dto.getAtencion() != null ?
                dto.getAtencion().getTipoIncidencia().getNombre() : "");
        exportDTO.setNombreUrgencia(dto.getAtencion() != null ?
                dto.getAtencion().getClasificacionUrgencia().getNombre() : "");
        exportDTO.setNombreClasificacionAtencion(dto.getAtencion() != null ?
                dto.getAtencion().getClasificacionAtencion().getNombre() : "");
        exportDTO.setNombreAreaAtencion(dto.getAtencion() != null ?
                dto.getAtencion().getAreaAtencion().getNombre() : "");
        exportDTO.setNombreSedeAtencion(dto.getAtencion() != null ?
                dto.getAtencion().getAreaAtencion().getSede().getNombre() : "");

        List<DetalleEnEspera> esperas = dto.getDetalleEnEspera();
        if (esperas.size() >= 1) {
            DetalleEnEspera e1 = esperas.get(0);
            exportDTO.setUsuarioEspera1(e1.getUsuario().getNombre());
            exportDTO.setClasificacionEspera1(e1.getClasificacionEspera().getNombre());
            exportDTO.setDescripcionEspera1(e1.getDescripcion());
            exportDTO.setFechaInicioEspera1(e1.ConvertirFechaConFormato(e1.getFechaInicio()));
            exportDTO.setHoraInicioEspera1(e1.ConvertirHoraConFormato(e1.getHoraInicio()));
            exportDTO.setFechaFinEspera1(e1.getFechaFin() != null ? e1.ConvertirFechaConFormato(e1.getFechaFin()) : "En curso");
            exportDTO.setHoraFinEspera1(e1.getHoraFin() != null ? e1.ConvertirHoraConFormato(e1.getHoraFin()) : "En curso");
        }

        if (esperas.size() >= 2) {
            DetalleEnEspera e2 = esperas.get(1);
            exportDTO.setUsuarioEspera2(e2.getUsuario().getNombre());
            exportDTO.setClasificacionEspera2(e2.getClasificacionEspera().getNombre());
            exportDTO.setDescripcionEspera2(e2.getDescripcion());
            exportDTO.setFechaInicioEspera2(e2.ConvertirFechaConFormato(e2.getFechaInicio()));
            exportDTO.setHoraInicioEspera2(e2.ConvertirHoraConFormato(e2.getHoraInicio()));
            exportDTO.setFechaFinEspera2(e2.getFechaFin() != null ? e2.ConvertirFechaConFormato(e2.getFechaFin()) : "En curso");
            exportDTO.setHoraFinEspera2(e2.getHoraFin() != null ? e2.ConvertirHoraConFormato(e2.getHoraFin()) : "En curso");
        }

        return exportDTO;
    }
}
