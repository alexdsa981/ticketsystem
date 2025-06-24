package com.ipor.ticketsystem.ticket.dto;

import com.ipor.ticketsystem.ticket.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DetalleTicketDTO {

    private Long id;
    private Ticket ticket;
    private Recepcion recepcion;
    private Atencion atencion;
    private Desestimacion desestimacion;
    private List<DetalleEnEspera> detalleEnEspera;

    // Fechas generales
    private String fechaFormateadaTicket;
    private String horaFormateadaTicket;
    private String fechaFormateadaRecepcion;
    private String horaFormateadaRecepcion;
    private String fechaFormateadaAtencion;
    private String horaFormateadaAtencion;
    private String fechaFormateadaDesestimacion;
    private String horaFormateadaDesestimacion;

    // Fechas y horas de espera 1
    private String fechaInicioEspera1;
    private String horaInicioEspera1;
    private String fechaFinEspera1;
    private String horaFinEspera1;

    // Fechas y horas de espera 2
    private String fechaInicioEspera2;
    private String horaInicioEspera2;
    private String fechaFinEspera2;
    private String horaFinEspera2;

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    public DetalleTicketDTO(Ticket ticket) {
        this.ticket = ticket;
        this.id = ticket.getId();
        this.fechaFormateadaTicket = ConvertirFechaConFormato(ticket.getFecha());
        this.horaFormateadaTicket = ConvertirHoraConFormato(ticket.getHora());

        this.detalleEnEspera = ticket.getListaDetalleEsperas();

        // Formatear detalles de espera si existen
        if (detalleEnEspera != null && !detalleEnEspera.isEmpty()) {
            if (detalleEnEspera.size() >= 1) {
                DetalleEnEspera e1 = detalleEnEspera.get(0);
                this.fechaInicioEspera1 = ConvertirFechaConFormato(e1.getFechaInicio());
                this.horaInicioEspera1 = ConvertirHoraConFormato(e1.getHoraInicio());
                this.fechaFinEspera1 = e1.getFechaFin() != null ? ConvertirFechaConFormato(e1.getFechaFin()) : "En curso";
                this.horaFinEspera1 = e1.getHoraFin() != null ? ConvertirHoraConFormato(e1.getHoraFin()) : "En curso";
            }
            if (detalleEnEspera.size() >= 2) {
                DetalleEnEspera e2 = detalleEnEspera.get(1);
                this.fechaInicioEspera2 = ConvertirFechaConFormato(e2.getFechaInicio());
                this.horaInicioEspera2 = ConvertirHoraConFormato(e2.getHoraInicio());
                this.fechaFinEspera2 = e2.getFechaFin() != null ? ConvertirFechaConFormato(e2.getFechaFin()) : "En curso";
                this.horaFinEspera2 = e2.getHoraFin() != null ? ConvertirHoraConFormato(e2.getHoraFin()) : "En curso";
            }
        }

        switch (ticket.getFaseTicket().getId().intValue()) {
            case 2:
            case 5:
                this.recepcion = ticket.getRecepcion();
                this.fechaFormateadaRecepcion = ConvertirFechaConFormato(recepcion.getFecha());
                this.horaFormateadaRecepcion = ConvertirHoraConFormato(recepcion.getHora());
                break;
            case 3:
                this.recepcion = ticket.getRecepcion();
                this.fechaFormateadaRecepcion = ConvertirFechaConFormato(recepcion.getFecha());
                this.horaFormateadaRecepcion = ConvertirHoraConFormato(recepcion.getHora());
                this.atencion = ticket.getAtencion();
                this.fechaFormateadaAtencion = ConvertirFechaConFormato(atencion.getFecha());
                this.horaFormateadaAtencion = ConvertirHoraConFormato(atencion.getHora());
                break;
            case 4:
                this.desestimacion = ticket.getDesestimacion();
                this.fechaFormateadaDesestimacion = ConvertirFechaConFormato(desestimacion.getFecha());
                this.horaFormateadaDesestimacion = ConvertirHoraConFormato(desestimacion.getHora());
                break;
        }
    }

    public String ConvertirFechaConFormato(LocalDate localDate) {
        return localDate.format(FORMATO_FECHA);
    }

    public String ConvertirHoraConFormato(LocalTime localTime) {
        return localTime.format(FORMATO_HORA);
    }
}
