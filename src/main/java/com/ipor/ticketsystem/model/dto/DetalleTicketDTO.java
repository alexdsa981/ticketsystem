package com.ipor.ticketsystem.model.dto;

import com.ipor.ticketsystem.model.dynamic.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class DetalleTicketDTO {
    private Long id;
    private Ticket ticket;
    private Recepcion recepcion;
    private Servicio servicio;
    private Desestimacion desestimacion;

    private String fechaFormateadaTicket;
    private String horaFormateadaTicket;

    private String fechaFormateadaRecepcion;
    private String horaFormateadaRecepcion;

    private String fechaFormateadaServicio;
    private String horaFormateadaServicio;

    private String fechaFormateadaDesestimacion;
    private String horaFormateadaDesestimacion;

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");



    //solo recepcion
    public DetalleTicketDTO(Ticket ticket){
        switch (ticket.getFaseTicket().getId().intValue()){
            case 1:
                this.ticket = ticket;
                this.fechaFormateadaTicket = ConvertirFechaConFormato(ticket.getFecha());
                this.horaFormateadaTicket = ConvertirHoraConFormato(ticket.getHora());
                break;
            case 2:
                this.ticket = ticket;
                this.fechaFormateadaTicket = ConvertirFechaConFormato(ticket.getFecha());
                this.horaFormateadaTicket = ConvertirHoraConFormato(ticket.getHora());

                this.recepcion = ticket.getRecepcion();
                this.fechaFormateadaRecepcion = ConvertirFechaConFormato(recepcion.getFecha());
                this.horaFormateadaRecepcion = ConvertirHoraConFormato(recepcion.getHora());
                break;
            case 3:
                this.ticket = ticket;
                this.fechaFormateadaTicket = ConvertirFechaConFormato(ticket.getFecha());
                this.horaFormateadaTicket = ConvertirHoraConFormato(ticket.getHora());

                this.recepcion = ticket.getRecepcion();
                this.fechaFormateadaRecepcion = ConvertirFechaConFormato(recepcion.getFecha());
                this.horaFormateadaRecepcion = ConvertirHoraConFormato(recepcion.getHora());

                this.servicio = ticket.getServicio();
                this.fechaFormateadaServicio = ConvertirFechaConFormato(servicio.getFecha());
                this.horaFormateadaServicio = ConvertirHoraConFormato(servicio.getHora());
                break;
            case 4:
                this.ticket = ticket;
                this.fechaFormateadaTicket = ConvertirFechaConFormato(ticket.getFecha());
                this.horaFormateadaTicket = ConvertirHoraConFormato(ticket.getHora());

                this.desestimacion = ticket.getDesestimacion();
                this.fechaFormateadaDesestimacion = ConvertirFechaConFormato(desestimacion.getFecha());
                this.horaFormateadaDesestimacion = ConvertirHoraConFormato(desestimacion.getHora());
                break;
        }
    }

    // Método para formatear la fecha como cadena
    public String ConvertirFechaConFormato(LocalDate localDate) {
        return localDate.format(FORMATO_FECHA);
    }

    // Método para formatear la hora como cadena
    public String ConvertirHoraConFormato(LocalTime localTime) {
        return localTime.format(FORMATO_HORA);
    }


}
