package com.ipor.ticketsystem.model.dto;

import com.ipor.ticketsystem.model.dynamic.*;
import com.ipor.ticketsystem.model.fixed.ClasificacionServicio;
import com.ipor.ticketsystem.model.fixed.ClasificacionUrgencia;
import com.ipor.ticketsystem.service.TicketService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AtencionTicketDTO {
    private Long id;
    private Usuario usuario;

    private TicketDTO ticket;
    private List<ArchivoAdjunto> adjuntosTicket;

    private ClasificacionUrgencia clasificacionUrgencia;
    private ClasificacionServicio clasificacionServicio;
    private String mensaje;
    private String descripcion;


    private LocalDate fecha;
    private LocalTime hora;
    private String fechaFormateada;
    private String horaFormateada;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("hh:mm");



    public AtencionTicketDTO(Recepcion recepcion, TicketService ticketService){
        this.id = recepcion.getId();
        this.ticket = new TicketDTO(recepcion.getTicket(), ticketService.getArchivosAdjuntosDeTicketPorTicketID(recepcion.getTicket().getId()));
        this.usuario = recepcion.getUsuario();
        this.clasificacionUrgencia=recepcion.getClasificacionUrgencia();
        this.mensaje = recepcion.getMensaje();
        this.fecha = recepcion.getFecha();
        this.fechaFormateada = getFechaConFormato();
        this.hora = recepcion.getHora();
        this.horaFormateada = getHoraConFormato();

    }
    public AtencionTicketDTO(Servicio servicio, TicketService ticketService){
        this.id = servicio.getId();
        this.ticket = new TicketDTO(servicio.getTicket(), ticketService.getArchivosAdjuntosDeTicketPorTicketID(servicio.getTicket().getId()));
        this.usuario = servicio.getUsuario();
        this.clasificacionServicio = servicio.getClasificacionServicio();
        this.descripcion = servicio.getDescripcion();
        this.fecha = servicio.getFecha();
        this.fechaFormateada = getFechaConFormato();
        this.hora = servicio.getHora();
        this.horaFormateada = getHoraConFormato();

    }

    // Método para formatear la fecha como cadena
    public String getFechaConFormato() {
        return this.fecha.format(FORMATO_FECHA);
    }

    // Método para formatear la hora como cadena
    public String getHoraConFormato() {
        return this.hora.format(FORMATO_HORA);
    }
}
