package com.ipor.ticketsystem.WebSocket;

import com.ipor.ticketsystem.model.dto.AtencionTicketDTO;
import com.ipor.ticketsystem.model.dto.NotificacionDTO;
import com.ipor.ticketsystem.model.dto.TicketDTO;
import com.ipor.ticketsystem.model.dto.otros.WebSocket.RecepcionRecordWS;
import com.ipor.ticketsystem.model.dto.otros.WebSocket.TicketRecordWS;
import com.ipor.ticketsystem.model.dynamic.Notificacion;
import com.ipor.ticketsystem.model.dynamic.Recepcion;
import com.ipor.ticketsystem.service.TicketService;
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

    //ENVIA Y OCULTA TICKET DE VISTA SOPORTE RECEPCION
    public void ocultarRegistroEnVistaSoporteRecepcion(Long idTicket) {
        String message = String.valueOf(idTicket);
        messagingTemplate.convertAndSend("/topic/ocultar/soporte-recepcion", message);
    }
    public void enviarTicketAVistaSoporteRecepcion(TicketDTO ticketDTO) {
        TicketRecordWS ticketRecordWS = new TicketRecordWS(ticketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/soporte-recepcion", ticketRecordWS);
    }
    public void enviarRecepcionAVistaSoporteAtencion(Recepcion recepcion, TicketService ticketService) {
        AtencionTicketDTO atencionTicketDTO = new AtencionTicketDTO(recepcion, ticketService);
        RecepcionRecordWS ticketRecordWS = new RecepcionRecordWS(atencionTicketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/soporte-atencion", ticketRecordWS);
    }



    //ENVIA Y OCULTA TICKET DE VISTA SOPORTE ATENCIÓN
    public void ocultarRegistroEnVistaSoporteAtencion(Long idTicket) {
        String message = String.valueOf(idTicket);
        messagingTemplate.convertAndSend("/topic/ocultar/soporte-atencion", message);
    }

    public void enviarAtencionAVistaSoporteHistorialAtencion(TicketDTO ticketDTO) {
        TicketRecordWS ticketRecordWS = new TicketRecordWS(ticketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/soporte-historial", ticketRecordWS);
    }




    //ENVIA Y OCULTA TICKET DE VISTA DIRECCIÓN REVISION
    public void ocultarRegistroEnVistaDireccionRevision(Long idTicket) {
        String message = String.valueOf(idTicket);
        messagingTemplate.convertAndSend("/topic/ocultar/direccion-revision", message);
    }
    public void enviarTicketAVistaDireccionRevision(TicketDTO ticketDTO) {
        TicketRecordWS ticketRecordWS = new TicketRecordWS(ticketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/direccion-revision", ticketRecordWS);
    }
    public void enviarTicketAVistaDireccionHistorial(TicketDTO ticketDTO) {
        TicketRecordWS ticketRecordWS = new TicketRecordWS(ticketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/direccion-historial", ticketRecordWS);
    }

}
