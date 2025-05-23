package com.ipor.ticketsystem.model.dto.otros.ExcelExport;

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
    private Date fechaAtencion;
    private Time horaAtencion;
    private String usuarioAtencion;
    private String clasAtencion;
    private Integer tiempoEsperaRecepcion;
    private Integer tiempoEsperaAtencion;
}
