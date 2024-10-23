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
    private String fechaFormateadaRecepcion;
    private String horaFormateadaRecepcion;
    private String fechaFormateadaServicio;
    private String horaFormateadaServicio;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");


    private Recepcion recepcionHistorial;
    private Servicio servicioHistorial;



    public AtencionTicketDTO(Recepcion recepcion, TicketService ticketService){
        this.id = recepcion.getId();
        this.ticket = new TicketDTO(recepcion.getTicket(), ticketService.getArchivosAdjuntosDeTicketPorTicketID(recepcion.getTicket().getId()));
        this.usuario = recepcion.getUsuario();
        this.clasificacionUrgencia=recepcion.getClasificacionUrgencia();
        this.mensaje = recepcion.getMensaje();
        this.fecha = recepcion.getFecha();
        this.fechaFormateadaRecepcion = ConvertirFechaConFormato(this.fecha);
        this.hora = recepcion.getHora();
        this.horaFormateadaRecepcion = ConvertirHoraConFormato(this.hora);

    }
    public AtencionTicketDTO(Servicio servicio, TicketService ticketService){
        this.id = servicio.getId();
        this.ticket = new TicketDTO(servicio.getTicket(), ticketService.getArchivosAdjuntosDeTicketPorTicketID(servicio.getTicket().getId()));
        this.usuario = servicio.getUsuario();
        this.clasificacionServicio = servicio.getClasificacionServicio();
        this.descripcion = servicio.getDescripcion();
        this.fecha = servicio.getFecha();
        this.fechaFormateadaServicio = ConvertirFechaConFormato(this.fecha);
        this.hora = servicio.getHora();
        this.horaFormateadaServicio = ConvertirHoraConFormato(this.hora);

    }
    public AtencionTicketDTO(Servicio servicio, Recepcion recepcion, TicketService ticketService){
        this.ticket = new TicketDTO(servicio.getTicket(), ticketService.getArchivosAdjuntosDeTicketPorTicketID(servicio.getTicket().getId()));
        this.recepcionHistorial = recepcion;
        this.servicioHistorial = servicio;

        this.fechaFormateadaRecepcion = ConvertirFechaConFormato(this.recepcionHistorial.getFecha());
        this.horaFormateadaRecepcion = ConvertirHoraConFormato(this.recepcionHistorial.getHora());

        this.fechaFormateadaServicio = ConvertirFechaConFormato(this.servicioHistorial.getFecha());
        this.horaFormateadaServicio = ConvertirHoraConFormato(this.servicioHistorial.getHora());



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
