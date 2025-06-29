package com.ipor.ticketsystem.model.dto.otros;

import java.time.LocalDate;

public interface TicketInactivoProjection {
    Long getId();
    String getCodigoTicket();
    String getDescripcion();
    String getNombreFase();
    String getNombreUsuario();
    LocalDate getUltimaFecha();
}
