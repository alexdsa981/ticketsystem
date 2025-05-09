package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.WebSocket.WSNotificacionesService;
import com.ipor.ticketsystem.model.dto.DetalleTicketDTO;
import com.ipor.ticketsystem.model.dynamic.*;
import com.ipor.ticketsystem.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

@Controller
@RequestMapping("/app/tickets")
public class AtencionController {
    @Autowired
    AtencionService atencionService;
    @Autowired
    TicketService ticketService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ClasificadoresService clasificadoresService;
    @Autowired
    NotificacionesService notificacionesService;
    @Autowired
    WSNotificacionesService WSNotificacionesService;

    //ESTA CLASE REPRESENTA LA LOGICA PARA MOSTRAR TICKETS DESDE LA FASE RECEPCIÓN
    // ATENDIDO O DESESTIMADO EN LAS VISTAS

    public Model getListaMisTicketsRecepcionadosAVista(Model model) {
        List<DetalleTicketDTO> MyRecepcionados = atencionService.getMyListaRecepcionados();
        model.addAttribute("MyRecepcionados", MyRecepcionados);
        return model;
    }

    public Model getListaTodosLosTicketsRecepcionadosAVista(Model model) {
        List<DetalleTicketDTO> AllRecepcionados = atencionService.getListaRecepcionados();
        model.addAttribute("AllRecepcionados", AllRecepcionados);
        return model;
    }


    public Model getListaMisTicketsAtendidosAVista(Model model) {
        List<DetalleTicketDTO> MyAtendidos = atencionService.getMyListaAtendidos();
        model.addAttribute("MyAtendidos", MyAtendidos);
        return model;
    }

    public Model getListaTodosLosTicketsAtendidosAVista(Model model) {
        List<DetalleTicketDTO> AllAtendidos = atencionService.getListaHistorialAtencion();
        model.addAttribute("AllAtendidos", AllAtendidos);
        return model;
    }

    public Model getListaMisTicketsDesestimadosAVista(Model model) {
        List<DetalleTicketDTO> MyDesestimados = atencionService.getMyListaDesestimados();
        model.addAttribute("MyDesestimados", MyDesestimados);
        return model;
    }

    public Model getListaTodosLosTicketsDesestimadosAVista(Model model) {
        List<DetalleTicketDTO> AllDesestimados = atencionService.getListaDesestimados();
        model.addAttribute("AllDesestimados", AllDesestimados);
        return model;
    }


    //metodo para recepcionar un ticket(desde soporte), crea un recepcionado y cambia la fase del ticket:
    @PostMapping("/recepcion/{id}")
    public ResponseEntity<String> recepcionarTicket(
            @RequestParam("mensaje") String mensaje,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        try {
            // Cambiar fase de ticket
            atencionService.updateFaseTicket(id, 2L);

            Ticket ticket = ticketService.getTicketPorID(id);

            // Crear la recepción
            Recepcion recepcion = new Recepcion();
            recepcion.setMensaje(mensaje);
            recepcion.setTicket(ticket);
            recepcion.setUsuario(usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado()));
            atencionService.saveRecepcion(recepcion);

            // Modificar el ticket
            ticket.setRecepcion(recepcion);
            ticketService.saveTicket(ticket);


            // Crear notificación
            Notificacion notificacion = new Notificacion();
            notificacion.setTicket(recepcion.getTicket());

            notificacion.setAbierto(Boolean.FALSE);
            notificacion.setLeido(Boolean.FALSE);
            notificacion.setUsuario(recepcion.getTicket().getUsuario());
            notificacion.setMensaje(" Ha sido Recepcionado");
            notificacion.setUrl("/ticket/"+ticket.getCodigoTicket());
            notificacionesService.saveNotiicacion(notificacion);
            WSNotificacionesService.enviarNotificacion(notificacion);
            WSNotificacionesService.ocultarRegistroEnVistaSoporteRecepcion(id);
            WSNotificacionesService.enviarRecepcionAVistaSoporteAtencion(ticket);
            WSNotificacionesService.enviarRecepcionAVistaUsuarioRecepcionados(ticket);
            WSNotificacionesService.ocultarRegistroEnVistaEnviadosUsuario(id);
            WSNotificacionesService.notificarActualizacionDashboard();
            WSNotificacionesService.notificarActualizacionPaginaTicket(ticket);

            // Redirigir a la URL actual
            String referer = request.getHeader("Referer");
            String redirectUrl = (referer != null ? referer : "/fallbackUrl") + "?successful=atencion";
            response.sendRedirect(redirectUrl);
            return ResponseEntity.ok("Ticket recepcionado correctamente");

        } catch (DataIntegrityViolationException e) {
            // Error de llave duplicada u otros problemas de integridad
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer + "?error=recepcion-duplicated" : "/fallbackUrl?error=recepcion-duplicated");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: El ticket ya ha sido recepcionado por otro usuario.");
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String fullStackTrace = sw.toString();

            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer + "?error=recepcion-general" : "/fallbackUrl?recepcion-error=general");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al recepcionar el ticket.");
        }
    }


    //metodo para atender un ticket, crea una atencion y cambia la fase del ticket:
    @PostMapping("/atencion/{id}")
    public ResponseEntity<String> atenderTicket(
            @RequestParam("descripcion") String descripcion,
            @RequestParam("area_atencion") Long IDarea_atencion,
            @RequestParam("clasificacion_urgencia") Long IDclasificacion_urgencia,
            @RequestParam("tipo_incidencia") Long ID_tipo_incidencia,
            @RequestParam("clasificacion_atencion") Long IDclasificacion_atencion,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            //cambiar fase de ticket
            atencionService.updateFaseTicket(id, 3L);

            Ticket ticket = ticketService.getTicketPorID(id);
            // Lógica para crear la atencion
            Atencion atencion = new Atencion();
            atencion.setDescripcion(descripcion);
            atencion.setClasificacionAtencion(clasificadoresService.getClasificacionAtencionPorId(IDclasificacion_atencion));
            atencion.setClasificacionUrgencia(clasificadoresService.getClasificacionUrgenciaPorId(IDclasificacion_urgencia));
            atencion.setTipoIncidencia(clasificadoresService.getTipoIncidenciaPorID(ID_tipo_incidencia));
            atencion.setAreaAtencion(clasificadoresService.getAreaAtencionPorId(IDarea_atencion));
            atencion.setTicket(ticket);
            atencion.setUsuario(usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado()));
            atencionService.saveAtencion(atencion);

            // Modificar el ticket
            ticket.setAtencion(atencion);
            ticketService.saveTicket(ticket);


            Notificacion notificacion = new Notificacion();
            notificacion.setTicket(atencion.getTicket());
            notificacion.setAbierto(Boolean.FALSE);
            notificacion.setLeido(Boolean.FALSE);
            notificacion.setUsuario(atencion.getTicket().getUsuario());
            notificacion.setMensaje(" Ha sido Atendido");
            notificacion.setUrl("/ticket/"+ticket.getCodigoTicket());
            notificacionesService.saveNotiicacion(notificacion);
            WSNotificacionesService.enviarNotificacion(notificacion);
            WSNotificacionesService.ocultarRegistroEnVistaSoporteAtencion(id);
            WSNotificacionesService.ocultarRegistroEnVistaUsuarioRecepcionados(id);
            WSNotificacionesService.enviarAtencionAVistaSoporteHistorialAtencion(ticket);
            WSNotificacionesService.enviarAtencionAVistaUsuarioAtendidos(ticket);
            WSNotificacionesService.notificarActualizacionDashboard();
            WSNotificacionesService.notificarActualizacionPaginaTicket(ticket);

            // Redirigir a la URL actual
            String referer = request.getHeader("Referer");
            String redirectUrl = (referer != null ? referer : "/fallbackUrl") + "?successful=atencion";
            response.sendRedirect(redirectUrl);
            return ResponseEntity.ok("Ticket atendido correctamente");
        } catch (DataIntegrityViolationException e) {
            // Error de llave duplicada u otros problemas de integridad
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer + "?error=atencion-duplicated" : "/fallbackUrl?error=atencion-duplicated");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: El ticket ya ha sido atendido por otro usuario.");
        } catch (Exception e) {
            // Captura otros errores
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer + "?error=atencion-general" : "/fallbackUrl?error=atencion-general");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al atender el ticket.");
        }

    }

    // Método para desestimar un ticket, crea una desestimación y cambia la fase del ticket:
    @PostMapping("/desestimacion/{id}")
    public ResponseEntity<String> desestimarTicket(
            @RequestParam("mensaje") String mensaje,
            @RequestParam("fase") int fase,
            @RequestParam("clasificacion_desestimacion") Long IDclasificacion_desestimacion,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            Ticket ticket = ticketService.getTicketPorID(id);

            Long lastFaseTicket = ticket.getFaseTicket().getId();

            if (fase != lastFaseTicket) {
                String referer = request.getHeader("Referer");
                String redirectUrl = (referer != null ? referer : "/fallbackUrl") + "?error=desestimacion-moved";
                response.sendRedirect(redirectUrl);
                return ResponseEntity.ok("Ticket ya fue movido a otra fase.");
            }


            // Cambiar fase del ticket
            atencionService.updateFaseTicket(id, 4L);

            // Lógica para crear la desestimación
            Desestimacion desestimacion = new Desestimacion();
            desestimacion.setDescripcion(mensaje);
            desestimacion.setClasificacionDesestimacion(clasificadoresService.getClasificacionDesestimacionPorId(IDclasificacion_desestimacion));
            desestimacion.setTicket(ticket);
            desestimacion.setUsuario(usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado()));
            atencionService.saveDesestimacion(desestimacion);

            // Modificar el ticket
            ticket.setDesestimacion(desestimacion);
            ticketService.saveTicket(ticket);

            Notificacion notificacion = new Notificacion();
            notificacion.setTicket(desestimacion.getTicket());
            notificacion.setAbierto(Boolean.FALSE);
            notificacion.setLeido(Boolean.FALSE);
            notificacion.setUsuario(desestimacion.getTicket().getUsuario());
            notificacion.setMensaje(" Ha sido Desestimado");
            notificacion.setUrl("/ticket/"+ticket.getCodigoTicket());

            notificacionesService.saveNotiicacion(notificacion);
            WSNotificacionesService.enviarNotificacion(notificacion);
            WSNotificacionesService.enviarAtencionAVistaSoporteHistorialDesestimacion(ticket);
            WSNotificacionesService.enviarAtencionAVistaUsuarioDesestimados(ticket);
            if (lastFaseTicket == 1L) {
                WSNotificacionesService.ocultarRegistroEnVistaSoporteRecepcion(id);
            } else if (lastFaseTicket == 2L) {
                WSNotificacionesService.ocultarRegistroEnVistaSoporteAtencion(id);
            }
            WSNotificacionesService.ocultarRegistroEnVistaUsuarioRecepcionados(id);
            WSNotificacionesService.ocultarRegistroEnVistaEnviadosUsuario(id);

            if (atencionService.findRecepcionByTicketID(id) != null) {
                atencionService.deleteRecepcion(atencionService.findRecepcionByTicketID(id));
            }
            WSNotificacionesService.notificarActualizacionDashboard();
            WSNotificacionesService.notificarActualizacionPaginaTicket(ticket);


            String referer = request.getHeader("Referer");
            String redirectUrl = (referer != null ? referer : "/fallbackUrl") + "?successful=desestimacion";
            response.sendRedirect(redirectUrl);
            return ResponseEntity.ok("Ticket desestimado correctamente");


        } catch (DataIntegrityViolationException e) {
            // Error de llave duplicada u otros problemas de integridad
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer + "?error=desestimacion-duplicated" : "/fallbackUrl?error=desestimacion-duplicated");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: El ticket ya ha sido desestimado por otro usuario.");
        } catch (Exception e) {
            // Captura otros errores
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer + "?error=desestimacion-general" : "/fallbackUrl?error=desestimacion-general");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al desestimar el ticket.");
        }

    }


}
