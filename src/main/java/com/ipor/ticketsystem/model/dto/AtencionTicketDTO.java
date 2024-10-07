package com.ipor.ticketsystem.model.dto;

import com.ipor.ticketsystem.model.dynamic.Recepcion;
import com.ipor.ticketsystem.model.dynamic.Servicio;
import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.model.fixed.ClasificacionServicio;
import com.ipor.ticketsystem.model.fixed.ClasificacionUrgencia;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class AtencionTicketDTO {
    private Long id;
    private Usuario usuarioAtencion;
    private Usuario usuarioTicket;

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



    public AtencionTicketDTO(Recepcion recepcion){
        this.id = recepcion.getId();
        this.usuarioAtencion = recepcion.getUsuario();
        this.clasificacionUrgencia=recepcion.getClasificacionUrgencia();
        this.mensaje = recepcion.getMensaje();
        this.fecha = recepcion.getFecha();
        this.fechaFormateada = getFechaConFormato();
        this.hora = recepcion.getHora();
        this.horaFormateada = getHoraConFormato();

    }
    public AtencionTicketDTO(Servicio servicio){
        this.id = servicio.getId();
        this.usuarioAtencion = servicio.getUsuario();
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
