package com.ipor.ticketsystem.model.dto;

import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.model.fixed.ClasificacionIncidencia;
import com.ipor.ticketsystem.model.fixed.FaseTicket;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TicketDTO {
    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private String descripcion;
    private Usuario usuario;
    private ClasificacionIncidencia clasificacionIncidencia;
    private FaseTicket faseTicket;
    private List<ArchivoAdjunto> listaArchivosAdjuntos;

    public TicketDTO(Ticket ticket, List<ArchivoAdjunto> adjuntos) {
        this.id = ticket.getId();
        this.fecha = ticket.getFecha();
        this.hora = ticket.getHora();
        this.descripcion = ticket.getDescripcion();
        this.usuario = ticket.getUsuario();
        this.clasificacionIncidencia = ticket.getClasificacionIncidencia();
        this.faseTicket = ticket.getFaseTicket();
        this.listaArchivosAdjuntos = adjuntos;
    }
}