package com.ipor.ticketsystem.WebSocket;

import com.ipor.ticketsystem.model.dto.AtencionTicketDTO;
import com.ipor.ticketsystem.model.dto.NotificacionDTO;
import com.ipor.ticketsystem.model.dto.TicketDTO;
import com.ipor.ticketsystem.model.dto.otros.WebSocket.AtencionRecordWS;
import com.ipor.ticketsystem.model.dto.otros.WebSocket.DesestimacionRecordWS;
import com.ipor.ticketsystem.model.dto.otros.WebSocket.RecepcionRecordWS;
import com.ipor.ticketsystem.model.dto.otros.WebSocket.TicketRecordWS;
import com.ipor.ticketsystem.model.dynamic.Desestimacion;
import com.ipor.ticketsystem.model.dynamic.Notificacion;
import com.ipor.ticketsystem.model.dynamic.Recepcion;
import com.ipor.ticketsystem.model.dynamic.Servicio;
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

    public void enviarAtencionAVistaSoporteHistorialAtencion(Servicio servicio, Recepcion recepcion, TicketService ticketService) {
        AtencionTicketDTO atencionTicketDTO = new AtencionTicketDTO(servicio,recepcion, ticketService);
        AtencionRecordWS ticketRecordWS = new AtencionRecordWS(atencionTicketDTO);
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

    //DESESTIMACIÓN
    public void enviarAtencionAVistaSoporteHistorialDesestimacion(Desestimacion desestimacion, TicketService ticketService) {
        AtencionTicketDTO atencionTicketDTO = new AtencionTicketDTO(desestimacion, ticketService);
        DesestimacionRecordWS ticketRecordWS = new DesestimacionRecordWS(atencionTicketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/desestimacion-historial", ticketRecordWS);
    }


    //VISTA ENVIADOS ENVIA Y OCULTA REGISTROS
    public void ocultarRegistroEnVistaEnviadosUsuario(Long idTicket) {
        String message = String.valueOf(idTicket);
        Long idUsuario = ticketService.getObtenerTicketPorID(idTicket).getUsuario().getId();
        messagingTemplate.convertAndSend("/topic/ocultar/usuario-enviados/"+ idUsuario, message);
    }
    public void enviarTicketAVistaEnviadosUsuario(TicketDTO ticketDTO) {
        TicketRecordWS ticketRecordWS = new TicketRecordWS(ticketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/usuario-enviados/"+ticketDTO.getUsuario().getId(), ticketRecordWS);
    }


    //VISTA RECEPCIONADOS USUARIO
    public void enviarRecepcionAVistaUsuarioRecepcionados(Recepcion recepcion, TicketService ticketService) {
        AtencionTicketDTO atencionTicketDTO = new AtencionTicketDTO(recepcion, ticketService);
        RecepcionRecordWS ticketRecordWS = new RecepcionRecordWS(atencionTicketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/usuario-recepcionados/"+recepcion.getTicket().getUsuario().getId(), ticketRecordWS);
    }
    public void ocultarRegistroEnVistaUsuarioRecepcionados(Long idTicket) {
        String message = String.valueOf(idTicket);
        Long idUsuario = ticketService.getObtenerTicketPorID(idTicket).getUsuario().getId();
        messagingTemplate.convertAndSend("/topic/ocultar/usuario-recepcionados/"+idUsuario, message);
    }
    //VISTA ATENDIDOS USUARIO
    public void enviarAtencionAVistaUsuarioAtendidos(Servicio servicio, Recepcion recepcion, TicketService ticketService) {
        AtencionTicketDTO atencionTicketDTO = new AtencionTicketDTO(servicio,recepcion, ticketService);
        AtencionRecordWS ticketRecordWS = new AtencionRecordWS(atencionTicketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/usuario-atendidos/"+recepcion.getTicket().getUsuario().getId(), ticketRecordWS);
    }
    //VISTA DESESTIMADOS USUARIO
    public void enviarAtencionAVistaUsuarioDesestimados(Desestimacion desestimacion, TicketService ticketService) {
        AtencionTicketDTO atencionTicketDTO = new AtencionTicketDTO(desestimacion, ticketService);
        DesestimacionRecordWS ticketRecordWS = new DesestimacionRecordWS(atencionTicketDTO);
        messagingTemplate.convertAndSend("/topic/actualizar/usuario-desestimados/"+desestimacion.getTicket().getUsuario().getId(), ticketRecordWS);
    }




}
