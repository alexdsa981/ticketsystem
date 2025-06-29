package com.ipor.ticketsystem.core.websocket;


import com.ipor.ticketsystem.core.websocket.dto.*;
import com.ipor.ticketsystem.ticket.dto.DetalleTicketDTO;
import com.ipor.ticketsystem.ticket.dto.NotificacionDTO;
import com.ipor.ticketsystem.ticket.service.TicketService;

import com.ipor.ticketsystem.ticket.comun.Notificacion;
import com.ipor.ticketsystem.ticket.Ticket;
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
        DetalleTicketDTO detalleTicketDTO = new DetalleTicketDTO(ticket);
        RecepcionRecordWS ticketRecordWS = new RecepcionRecordWS(detalleTicketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/soporte-atencion", ticketRecordWS);
    }





    //ENVIA Y OCULTA TICKET DE VISTA EN ESPERA SOPORTE
    public void ocultarRegistroEnVistaSoporteEnEspera(Long idTicket) {
        String message = String.valueOf(idTicket);
        messagingTemplate.convertAndSend("/topic/ocultar/soporte-espera", message);
    }
    public void enviarTicketAVistaSoporteEnEspera(Ticket ticket) {
        DetalleTicketDTO detalleTicketDTO = new DetalleTicketDTO(ticket);
        EsperaRecordWS ticketRecordWS = new EsperaRecordWS(detalleTicketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/soporte-espera", ticketRecordWS);
    }





    //ENVIA Y OCULTA TICKET DE VISTA SOPORTE ATENCIÓN
    public void ocultarRegistroEnVistaSoporteAtencion(Long idTicket) {
        String message = String.valueOf(idTicket);
        messagingTemplate.convertAndSend("/topic/ocultar/soporte-atencion", message);
    }

    public void enviarAtencionAVistaSoporteHistorialAtencion(Ticket ticket) {
        DetalleTicketDTO detalleTicketDTO = new DetalleTicketDTO(ticket);
        AtencionRecordWS ticketRecordWS = new AtencionRecordWS(detalleTicketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/soporte-historial", ticketRecordWS);
    }


    //DESESTIMACIÓN
    public void enviarAtencionAVistaSoporteHistorialDesestimacion(Ticket ticket ) {
        DetalleTicketDTO detalleTicketDTO = new DetalleTicketDTO(ticket);
        DesestimacionRecordWS ticketRecordWS = new DesestimacionRecordWS(detalleTicketDTO);
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



    //VISTA ENVIADOS ENVIA Y OCULTA REGISTROS EN ESPERA USUARIO
    public void ocultarRegistroEnVistaEnEsperaUsuario(Long idTicket) {
        String message = String.valueOf(idTicket);
        Long idUsuario = ticketService.getTicketPorID(idTicket).getUsuario().getId();
        messagingTemplate.convertAndSend("/topic/ocultar/usuario-espera/"+ idUsuario, message);
    }
    public void enviarTicketAVistaEnEsperaUsuario(Ticket ticket) {
        DetalleTicketDTO detalleTicketDTO = new DetalleTicketDTO(ticket);
        EsperaRecordWS ticketRecordWS = new EsperaRecordWS(detalleTicketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/usuario-espera/"+ticket.getUsuario().getId(), ticketRecordWS);
    }





    //VISTA RECEPCIONADOS USUARIO
    public void enviarRecepcionAVistaUsuarioRecepcionados(Ticket ticket) {
        DetalleTicketDTO detalleTicketDTO = new DetalleTicketDTO(ticket);
        RecepcionRecordWS ticketRecordWS = new RecepcionRecordWS(detalleTicketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/usuario-recepcionados/"+ticket.getUsuario().getId(), ticketRecordWS);
    }
    public void ocultarRegistroEnVistaUsuarioRecepcionados(Long idTicket) {
        String message = String.valueOf(idTicket);
        Long idUsuario = ticketService.getTicketPorID(idTicket).getUsuario().getId();
        messagingTemplate.convertAndSend("/topic/ocultar/usuario-recepcionados/"+idUsuario, message);
    }


    //VISTA ATENDIDOS USUARIO
    public void enviarAtencionAVistaUsuarioAtendidos(Ticket ticket) {
        DetalleTicketDTO detalleTicketDTO = new DetalleTicketDTO(ticket);
        AtencionRecordWS ticketRecordWS = new AtencionRecordWS(detalleTicketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/usuario-atendidos/"+ticket.getUsuario().getId(), ticketRecordWS);
    }
    //VISTA DESESTIMADOS USUARIO
    public void enviarAtencionAVistaUsuarioDesestimados(Ticket ticket) {
        DetalleTicketDTO detalleTicketDTO = new DetalleTicketDTO(ticket);
        DesestimacionRecordWS ticketRecordWS = new DesestimacionRecordWS(detalleTicketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/usuario-desestimados/"+ticket.getUsuario().getId(), ticketRecordWS);
    }

    public void notificarActualizacionEstadoActual() {
        messagingTemplate.convertAndSend("/topic/estadoActual", "actualizar");
    }

    public void notificarActualizacionPaginaTicket(Ticket ticket) {
        messagingTemplate.convertAndSend("/topic/actualizar/ticket/" + ticket.getCodigoTicket(), "actualizar");
    }





}
