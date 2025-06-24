package com.ipor.ticketsystem.dashboard.soporte;

import java.time.LocalDate;

public interface TicketInactivoProjection {
    Long getId();
    String getCodigoTicket();
    String getDescripcion();
    String getNombreFase();
    String getNombreUsuario();
    LocalDate getUltimaFecha();
}
