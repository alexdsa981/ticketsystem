package com.ipor.ticketsystem.model.dto;

import com.ipor.ticketsystem.model.dynamic.Notificacion;

import java.time.LocalDate;
import java.time.LocalTime;


public record NotificacionDTO(
        Long id,
        String idFormateado,
        String fechaFormateada,
        String horaFormateada,
        String descripcion,
        String nombreUsuario,
        String url,
        boolean leido,
        boolean abierto
)


{
    public NotificacionDTO(Notificacion notificacion) {
        this(
                notificacion.getId(),
                String.format("TK-%04d", notificacion.getTicket().getId()),
                notificacion.getFecha().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), // Formatear la fecha inline
                notificacion.getHora().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")), // Formatear la hora inline
                notificacion.getMensaje(),
                notificacion.getUsuario().getNombre(),
                notificacion.getUrl(),
                notificacion.getLeido(),
                notificacion.getAbierto()
        );
    }
}
