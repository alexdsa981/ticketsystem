package com.ipor.ticketsystem.WebSocket;

import com.ipor.ticketsystem.model.dto.NotificacionDTO;
import com.ipor.ticketsystem.model.dto.TicketDTO;
import com.ipor.ticketsystem.model.dto.otros.TicketRecordWS;
import com.ipor.ticketsystem.model.dynamic.Notificacion;
import com.ipor.ticketsystem.service.UsuarioService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WSNotificacionesService {
    private final UsuarioService usuarioService;
    private final SimpMessagingTemplate messagingTemplate;

    public WSNotificacionesService(UsuarioService usuarioService, SimpMessagingTemplate messagingTemplate) {
        this.usuarioService = usuarioService;
        this.messagingTemplate = messagingTemplate;
    }

    public void enviarNotificacion(Notificacion notificacion) {
        NotificacionDTO notificacionDTO = new NotificacionDTO(notificacion);
        String message = notificacionDTO.idFormateado() + ": "+ notificacionDTO.descripcion();
        messagingTemplate.convertAndSend("/topic/notificaciones/" + notificacion.getUsuario().getId(), message);
    }

    public void enviarTicketAVistaRecepción(TicketDTO ticketDTO) {
        TicketRecordWS ticketRecordWS = new TicketRecordWS(ticketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/soporte-recepcion", ticketRecordWS);
    }

    public void ocultarRegistroEnVistaSoporteRecepcion(Long idTicket) {
        String message = String.valueOf(idTicket);
        messagingTemplate.convertAndSend("/topic/ocultar/soporte-recepcion", message);
    }

    public void ocultarRegistroEnVistaSoporteAtencion(Long idTicket) {
        String message = String.valueOf(idTicket);
        messagingTemplate.convertAndSend("/topic/ocultar/soporte-atencion", message);
    }

}
