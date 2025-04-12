package com.ipor.ticketsystem.model.dto;

import com.ipor.ticketsystem.model.dynamic.*;
import com.ipor.ticketsystem.model.fixed.ClasificacionArea;
import com.ipor.ticketsystem.model.fixed.ClasificacionDesestimacion;
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

    private ClasificacionDesestimacion clasificacionDesestimacion;
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
    private String fechaFormateadaDesestimacion;
    private String horaFormateadaDesestimacion;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");


    private Recepcion recepcionHistorial;
    private Servicio servicioHistorial;



    //solo recepcion
    public AtencionTicketDTO(Recepcion recepcion, TicketService ticketService){
        this.id = recepcion.getId();
        this.ticket = new TicketDTO(recepcion.getTicket());
        this.usuario = recepcion.getUsuario();
        this.clasificacionUrgencia=recepcion.getClasificacionUrgencia();
        this.mensaje = recepcion.getMensaje();
        this.fecha = recepcion.getFecha();
        this.fechaFormateadaRecepcion = ConvertirFechaConFormato(this.fecha);
        this.hora = recepcion.getHora();
        this.horaFormateadaRecepcion = ConvertirHoraConFormato(this.hora);

    }
    //solo servicio
    public AtencionTicketDTO(Servicio servicio, TicketService ticketService){
        this.id = servicio.getId();
        this.ticket = new TicketDTO(servicio.getTicket());
        this.usuario = servicio.getUsuario();
        this.clasificacionServicio = servicio.getClasificacionServicio();
        this.descripcion = servicio.getDescripcion();
        this.fecha = servicio.getFecha();
        this.fechaFormateadaServicio = ConvertirFechaConFormato(this.fecha);
        this.hora = servicio.getHora();
        this.horaFormateadaServicio = ConvertirHoraConFormato(this.hora);

    }
    //combina recepcion y servicio para historial
    public AtencionTicketDTO(Servicio servicio, Recepcion recepcion, TicketService ticketService){
        this.ticket = new TicketDTO(servicio.getTicket());
        this.recepcionHistorial = recepcion;
        this.servicioHistorial = servicio;

        this.fechaFormateadaRecepcion = ConvertirFechaConFormato(this.recepcionHistorial.getFecha());
        this.horaFormateadaRecepcion = ConvertirHoraConFormato(this.recepcionHistorial.getHora());

        this.fechaFormateadaServicio = ConvertirFechaConFormato(this.servicioHistorial.getFecha());
        this.horaFormateadaServicio = ConvertirHoraConFormato(this.servicioHistorial.getHora());

    }

    public AtencionTicketDTO(Desestimacion desestimacion , TicketService ticketService){
        this.ticket = new TicketDTO(desestimacion.getTicket());
        this.fecha = desestimacion.getFecha();
        this.fechaFormateadaDesestimacion = ConvertirFechaConFormato(this.fecha);
        this.hora = desestimacion.getHora();
        this.horaFormateadaDesestimacion = ConvertirHoraConFormato(this.hora);
        this.descripcion = desestimacion.getDescripcion();
        this.id = desestimacion.getId();
        this.usuario = desestimacion.getUsuario();
        this.clasificacionDesestimacion = desestimacion.getClasificacionDesestimacion();
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
