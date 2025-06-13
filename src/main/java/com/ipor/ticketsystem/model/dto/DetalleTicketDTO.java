package com.ipor.ticketsystem.model.dto;

import com.ipor.ticketsystem.model.dynamic.*;
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

    private String fechaFormateadaTicket;
    private String horaFormateadaTicket;

    private String fechaFormateadaRecepcion;
    private String horaFormateadaRecepcion;

    private String fechaFormateadaAtencion;
    private String horaFormateadaAtencion;

    private String fechaFormateadaDesestimacion;
    private String horaFormateadaDesestimacion;

    private String fechaFormateadaEnEspera;
    private String horaFormateadaEnEspera;



    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");



    //solo recepcion
    public DetalleTicketDTO(Ticket ticket){
        switch (ticket.getFaseTicket().getId().intValue()){
            case 1:
                this.ticket = ticket;
                this.fechaFormateadaTicket = ConvertirFechaConFormato(ticket.getFecha());
                this.horaFormateadaTicket = ConvertirHoraConFormato(ticket.getHora());

                this.detalleEnEspera = ticket.getListaDetalleEsperas();

                break;
            case 2:
                this.ticket = ticket;
                this.fechaFormateadaTicket = ConvertirFechaConFormato(ticket.getFecha());
                this.horaFormateadaTicket = ConvertirHoraConFormato(ticket.getHora());

                this.recepcion = ticket.getRecepcion();
                this.fechaFormateadaRecepcion = ConvertirFechaConFormato(recepcion.getFecha());
                this.horaFormateadaRecepcion = ConvertirHoraConFormato(recepcion.getHora());

                this.detalleEnEspera = ticket.getListaDetalleEsperas();

                break;
            case 3:
                this.ticket = ticket;
                this.fechaFormateadaTicket = ConvertirFechaConFormato(ticket.getFecha());
                this.horaFormateadaTicket = ConvertirHoraConFormato(ticket.getHora());

                this.recepcion = ticket.getRecepcion();
                this.fechaFormateadaRecepcion = ConvertirFechaConFormato(recepcion.getFecha());
                this.horaFormateadaRecepcion = ConvertirHoraConFormato(recepcion.getHora());

                this.atencion = ticket.getAtencion();
                this.fechaFormateadaAtencion = ConvertirFechaConFormato(atencion.getFecha());
                this.horaFormateadaAtencion = ConvertirHoraConFormato(atencion.getHora());

                this.detalleEnEspera = ticket.getListaDetalleEsperas();

                break;
            case 4:
                this.ticket = ticket;
                this.fechaFormateadaTicket = ConvertirFechaConFormato(ticket.getFecha());
                this.horaFormateadaTicket = ConvertirHoraConFormato(ticket.getHora());

                this.desestimacion = ticket.getDesestimacion();
                this.fechaFormateadaDesestimacion = ConvertirFechaConFormato(desestimacion.getFecha());
                this.horaFormateadaDesestimacion = ConvertirHoraConFormato(desestimacion.getHora());

                this.detalleEnEspera = ticket.getListaDetalleEsperas();

                break;
            case 5:
                this.ticket = ticket;
                this.fechaFormateadaTicket = ConvertirFechaConFormato(ticket.getFecha());
                this.horaFormateadaTicket = ConvertirHoraConFormato(ticket.getHora());

                this.recepcion = ticket.getRecepcion();
                this.fechaFormateadaRecepcion = ConvertirFechaConFormato(recepcion.getFecha());
                this.horaFormateadaRecepcion = ConvertirHoraConFormato(recepcion.getHora());

                this.detalleEnEspera = ticket.getListaDetalleEsperas();
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
