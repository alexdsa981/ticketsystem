package com.ipor.ticketsystem.WebSocket;

import com.ipor.ticketsystem.model.dto.TicketDTO;
import com.ipor.ticketsystem.model.dto.otros.TicketRecordWS;
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

    public void enviarAlertaASoporte(TicketDTO ticketDTO) {
        TicketRecordWS ticketRecordWS = new TicketRecordWS(ticketDTO);
        String message = "Ticket Recibido: "+ticketRecordWS.nombreUsuario() + " - " + ticketRecordWS.nombreClasificacionIncidencia();
        messagingTemplate.convertAndSend("/topic/notificaciones/soporte", message);

    }
    public void enviarTicket(TicketDTO ticketDTO) {
        TicketRecordWS ticketRecordWS = new TicketRecordWS(ticketDTO);
        messagingTemplate.convertAndSend("/topic/tickets", ticketRecordWS);
    }
    public void aumentarNumeroNotificacion(Long id) {
        String message = "Contador Actualizado desde wsnotificacionesService"; // O cualquier contenido que tenga sentido
        messagingTemplate.convertAndSend("/topic/notificaciones/contador/" + id, message);
    }

}
