package com.ipor.ticketsystem.model.dto.otros;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TicketExportDTO {
    private int idTicket;
    private String faseTicket;
    private Date fechaTicket;
    private Time horaTicket;
    private String usuarioTicket;
    private String clasIncidencia;
    private Date fechaRecepcion;
    private Time horaRecepcion;
    private String usuarioRecepcion;
    private String clasUrgencia;
    private Date fechaServicio;
    private Time horaServicio;
    private String usuarioServicio;
    private String clasServicio;
    private Integer tiempoEsperaRecepcion;
    private Integer tiempoEsperaServicio;
}
