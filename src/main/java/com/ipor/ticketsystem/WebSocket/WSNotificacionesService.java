package com.ipor.ticketsystem.WebSocket;


import com.ipor.ticketsystem.model.dto.DetalleTicketDTO;
import com.ipor.ticketsystem.model.dto.NotificacionDTO;
import com.ipor.ticketsystem.model.dto.otros.WebSocket.AtencionRecordWS;
import com.ipor.ticketsystem.model.dto.otros.WebSocket.DesestimacionRecordWS;
import com.ipor.ticketsystem.model.dto.otros.WebSocket.RecepcionRecordWS;
import com.ipor.ticketsystem.model.dto.otros.WebSocket.TicketRecordWS;
import com.ipor.ticketsystem.model.dynamic.*;
import com.ipor.ticketsystem.service.TicketService;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WSNotificacionesService {

    private final SimpMessagingTemplate messagingTemplate;
    private final TicketService ticketService;

    public WSNotificacionesService( SimpMessagingTemplate messagingTemplate, TicketService ticketService) {
        this.messagingTemplate = messagingTemplate;
        this.ticketService = ticketService;
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
    public void enviarTicketAVistaSoporteRecepcion(DetalleTicketDTO detalle) {
        TicketRecordWS ticketRecordWS = new TicketRecordWS(detalle);
        messagingTemplate.convertAndSend("/topic/actualizar/soporte-recepcion", ticketRecordWS);
    }

    public void enviarRecepcionAVistaSoporteAtencion(Ticket ticket) {

        DetalleTicketDTO atencionTicketDTO = new DetalleTicketDTO(ticket);
        RecepcionRecordWS ticketRecordWS = new RecepcionRecordWS(atencionTicketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/soporte-atencion", ticketRecordWS);
    }




    //ENVIA Y OCULTA TICKET DE VISTA SOPORTE ATENCIÓN
    public void ocultarRegistroEnVistaSoporteAtencion(Long idTicket) {
        String message = String.valueOf(idTicket);
        messagingTemplate.convertAndSend("/topic/ocultar/soporte-atencion", message);
    }

    public void enviarAtencionAVistaSoporteHistorialAtencion(Ticket ticket) {
        DetalleTicketDTO atencionTicketDTO = new DetalleTicketDTO(ticket);
        AtencionRecordWS ticketRecordWS = new AtencionRecordWS(atencionTicketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/soporte-historial", ticketRecordWS);
    }


    //DESESTIMACIÓN
    public void enviarAtencionAVistaSoporteHistorialDesestimacion(Ticket ticket ) {
        DetalleTicketDTO atencionTicketDTO = new DetalleTicketDTO(ticket);
        DesestimacionRecordWS ticketRecordWS = new DesestimacionRecordWS(atencionTicketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/desestimacion-historial", ticketRecordWS);
    }


    //VISTA ENVIADOS ENVIA Y OCULTA REGISTROS
    public void ocultarRegistroEnVistaEnviadosUsuario(Long idTicket) {
        String message = String.valueOf(idTicket);
        Long idUsuario = ticketService.getTicketPorID(idTicket).getUsuario().getId();
        messagingTemplate.convertAndSend("/topic/ocultar/usuario-enviados/"+ idUsuario, message);
    }
    public void enviarTicketAVistaEnviadosUsuario(DetalleTicketDTO detalle) {
        TicketRecordWS ticketRecordWS = new TicketRecordWS(detalle);
        messagingTemplate.convertAndSend("/topic/actualizar/usuario-enviados/"+detalle.getTicket().getUsuario().getId(), ticketRecordWS);
    }


    //VISTA RECEPCIONADOS USUARIO
    public void enviarRecepcionAVistaUsuarioRecepcionados(Ticket ticket) {
        DetalleTicketDTO atencionTicketDTO = new DetalleTicketDTO(ticket);
        RecepcionRecordWS ticketRecordWS = new RecepcionRecordWS(atencionTicketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/usuario-recepcionados/"+ticket.getUsuario().getId(), ticketRecordWS);
    }
    public void ocultarRegistroEnVistaUsuarioRecepcionados(Long idTicket) {
        String message = String.valueOf(idTicket);
        Long idUsuario = ticketService.getTicketPorID(idTicket).getUsuario().getId();
        messagingTemplate.convertAndSend("/topic/ocultar/usuario-recepcionados/"+idUsuario, message);
    }
    //VISTA ATENDIDOS USUARIO
    public void enviarAtencionAVistaUsuarioAtendidos(Ticket ticket) {
        DetalleTicketDTO atencionTicketDTO = new DetalleTicketDTO(ticket);
        AtencionRecordWS ticketRecordWS = new AtencionRecordWS(atencionTicketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/usuario-atendidos/"+ticket.getUsuario().getId(), ticketRecordWS);
    }
    //VISTA DESESTIMADOS USUARIO
    public void enviarAtencionAVistaUsuarioDesestimados(Ticket ticket) {
        DetalleTicketDTO atencionTicketDTO = new DetalleTicketDTO(ticket);
        DesestimacionRecordWS ticketRecordWS = new DesestimacionRecordWS(atencionTicketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/usuario-desestimados/"+ticket.getUsuario().getId(), ticketRecordWS);
    }




}
