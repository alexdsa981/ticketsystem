package com.ipor.ticketsystem.model.dto;

import com.ipor.ticketsystem.model.dynamic.*;
import com.ipor.ticketsystem.model.fixed.ClasificacionArea;
import com.ipor.ticketsystem.model.fixed.ClasificacionIncidencia;
import com.ipor.ticketsystem.model.fixed.FaseTicket;

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
public class TicketDTO {
    private Long id;
    private String idFormateado;
    private LocalDate fecha;
    private LocalTime hora;
    private String fechaFormateada;
    private String horaFormateada;
    private String descripcion;
    private Usuario usuario;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");
    private ClasificacionIncidencia clasificacionIncidencia;
    private ClasificacionArea clasificacionArea;
    private FaseTicket faseTicket;
    private List<ArchivoAdjunto> listaArchivosAdjuntos;


    public TicketDTO(Ticket ticket) {
        this.id = ticket.getId();
        this.idFormateado = getIdConFormato();
        this.fecha = ticket.getFecha();
        this.fechaFormateada = getFechaConFormato();
        this.hora = ticket.getHora();
        this.horaFormateada = getHoraConFormato();
        this.descripcion = ticket.getDescripcion();
        this.usuario = ticket.getUsuario();
        this.clasificacionIncidencia = ticket.getClasificacionIncidencia();
        this.clasificacionArea = ticket.getClasificacionArea();
        this.faseTicket = ticket.getFaseTicket();
        this.listaArchivosAdjuntos = ticket.getListaArchivosAdjuntos();
    }

    // Método para formatear la fecha como cadena
    public String getFechaConFormato() {
        return this.fecha.format(FORMATO_FECHA);
    }

    // Método para formatear la hora como cadena
    public String getHoraConFormato() {
        return this.hora.format(FORMATO_HORA);
    }

    // Método para obtener el ID formateado
    public String getIdConFormato() {
        return this.fechaFormateada = String.format("TK-%04d", id);
    }
}
