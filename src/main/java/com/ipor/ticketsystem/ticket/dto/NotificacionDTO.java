package com.ipor.ticketsystem.ticket.dto;

import com.ipor.ticketsystem.ticket.comun.Notificacion;


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
                notificacion.getTicket().getCodigoTicket(),
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
