package com.ipor.ticketsystem.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipor.ticketsystem.WebSocket.WSNotificacionesService;
import com.ipor.ticketsystem.model.dto.AtencionTicketDTO;
import com.ipor.ticketsystem.model.dto.TicketDTO;
import com.ipor.ticketsystem.model.dynamic.*;
import com.ipor.ticketsystem.model.fixed.ClasificacionArea;
import com.ipor.ticketsystem.model.fixed.ClasificacionIncidencia;
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
        List<AtencionTicketDTO> MyRecepcionados = atencionService.getMyListaRecepcionados();
        Collections.reverse(MyRecepcionados);
        model.addAttribute("MyRecepcionados", MyRecepcionados);
        return model;
    }

    public Model getListaTodosLosTicketsRecepcionadosAVista(Model model) {
        List<AtencionTicketDTO> AllRecepcionados = atencionService.getListaRecepcionados();
        model.addAttribute("AllRecepcionados", AllRecepcionados);
        return model;
    }


    public Model getListaMisTicketsAtendidosAVista(Model model) {
        List<AtencionTicketDTO> MyAtendidos = atencionService.getMyListaAtendidos();
        model.addAttribute("MyAtendidos", MyAtendidos);
        return model;
    }

    public Model getListaTodosLosTicketsAtendidosAVista(Model model) {
        List<AtencionTicketDTO> AllAtendidos = atencionService.getListaHistorialAtencion();
        model.addAttribute("AllAtendidos", AllAtendidos);
        return model;
    }

    public Model getListaMisTicketsDesestimadosAVista(Model model) {
        List<AtencionTicketDTO> MyDesestimados = atencionService.getMyListaDesestimados();
        Collections.reverse(MyDesestimados);
        model.addAttribute("MyDesestimados", MyDesestimados);
        return model;
    }

    public Model getListaTodosLosTicketsDesestimadosAVista(Model model) {
        List<AtencionTicketDTO> AllDesestimados = atencionService.getListaDesestimados();
        Collections.reverse(AllDesestimados);
        model.addAttribute("AllDesestimados", AllDesestimados);
        return model;
    }


    //metodo para recepcionar un ticket(desde soporte), crea un recepcionado y cambia la fase del ticket:
    @PostMapping("/recepcion/{id}")
    public ResponseEntity<String> recepcionarTicket(
            @RequestParam("mensaje") String mensaje,
            @RequestParam("clasificacion_urgencia") Long IDclasificacion_urgencia,
            @RequestParam("clasificacion_incidencia") Long ID_clasificacion_incidencia,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        try {
            // Cambiar fase de ticket
            atencionService.updateFaseTicket(id, 2L);

            // Crear la recepción
            Recepcion recepcion = new Recepcion();
            recepcion.setMensaje(mensaje);
            recepcion.setClasificacionUrgencia(clasificadoresService.getClasificacionUrgenciaPorId(IDclasificacion_urgencia));
            recepcion.setTicket(ticketService.getObtenerTicketPorID(id));
            recepcion.setUsuario(usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado()));
            recepcion.setHora(recepcion.getHora());
            recepcion.setFecha(recepcion.getFecha());
            atencionService.saveRecepcion(recepcion);

            // Modificar el ticket
            ClasificacionIncidencia clasificacionIncidencia = clasificadoresService.getClasificacionIncidenciaPorID(ID_clasificacion_incidencia);
            recepcion.getTicket().setClasificacionIncidencia(clasificacionIncidencia);
            ticketService.saveTicket(recepcion.getTicket());

            // Crear notificación
            Notificacion notificacion = new Notificacion();
            notificacion.setTicket(recepcion.getTicket());
            notificacion.setHora(notificacion.getHora());
            notificacion.setFecha(notificacion.getFecha());
            notificacion.setAbierto(Boolean.FALSE);
            notificacion.setLeido(Boolean.FALSE);
            notificacion.setUsuario(recepcion.getTicket().getUsuario());
            notificacion.setMensaje(" Ha sido Recepcionado");
            notificacion.setUrl("/TicketsEnProceso");
            notificacionesService.saveNotiicacion(notificacion);
            WSNotificacionesService.enviarNotificacion(notificacion);
            WSNotificacionesService.ocultarRegistroEnVistaSoporteRecepcion(id);
            WSNotificacionesService.enviarRecepcionAVistaSoporteAtencion(recepcion, ticketService);
            WSNotificacionesService.enviarRecepcionAVistaUsuarioRecepcionados(recepcion, ticketService);
            WSNotificacionesService.ocultarRegistroEnVistaEnviadosUsuario(id);

            // Redirigir a la URL actual
            String referer = request.getHeader("Referer");
            response.sendRedirect("/soporte/Recepcionar?successful=recepcion");
            return ResponseEntity.ok("Ticket recepcionado correctamente");

        } catch (DataIntegrityViolationException e) {
            // Error de llave duplicada u otros problemas de integridad
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer + "?error=recepcion-duplicated" : "/fallbackUrl?error=recepcion-duplicated");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: El ticket ya ha sido recepcionado por otro usuario.");
        } catch (Exception e) {
            // Captura otros errores
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer + "?error=recepcion-general" : "/fallbackUrl?recepcion-error=general");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al recepcionar el ticket.");
        }
    }


    //metodo para atender un ticket, crea un atendido(servicio) y cambia la fase del ticket:
    @PostMapping("/atencion/{id}")
    public ResponseEntity<String> atenderTicket(
            @RequestParam("descripcion") String descripcion,
            @RequestParam("clasificacion_area") Long IDclasificacion_area,
            @RequestParam("clasificacion_servicio") Long IDclasificacion_servicio,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            //cambiar fase de ticket
            atencionService.updateFaseTicket(id, 3L);
            // Lógica para crear la atencion
            Servicio servicio = new Servicio();
            servicio.setDescripcion(descripcion);
            servicio.setClasificacionServicio(clasificadoresService.getClasificacionServicioPorId(IDclasificacion_servicio));
            servicio.setTicket(ticketService.getObtenerTicketPorID(id));
            servicio.setUsuario(usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado()));
            servicio.setHora(servicio.getHora());
            servicio.setFecha(servicio.getFecha());
            atencionService.saveServicio(servicio);

            // Modificar el ticket
            ClasificacionArea clasificacionArea = clasificadoresService.getClasificacionAreaPorId(IDclasificacion_area);
            servicio.getTicket().setClasificacionArea(clasificacionArea);
            ticketService.saveTicket(servicio.getTicket());


            Notificacion notificacion = new Notificacion();
            notificacion.setTicket(servicio.getTicket());
            notificacion.setHora(notificacion.getHora());
            notificacion.setFecha(notificacion.getFecha());
            notificacion.setAbierto(Boolean.FALSE);
            notificacion.setLeido(Boolean.FALSE);
            notificacion.setUsuario(servicio.getTicket().getUsuario());
            notificacion.setMensaje(" Ha sido Atendido");
            notificacion.setUrl("/TicketsAtendidos");
            notificacionesService.saveNotiicacion(notificacion);
            WSNotificacionesService.enviarNotificacion(notificacion);
            WSNotificacionesService.ocultarRegistroEnVistaSoporteAtencion(id);
            WSNotificacionesService.ocultarRegistroEnVistaUsuarioRecepcionados(id);
            Recepcion recepcion = atencionService.findRecepcionByTicketID(id);
            WSNotificacionesService.enviarAtencionAVistaSoporteHistorialAtencion(servicio, recepcion, ticketService);
            WSNotificacionesService.enviarAtencionAVistaUsuarioAtendidos(servicio, recepcion, ticketService);

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
            System.out.println(e);
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
            Ticket ticket = ticketService.getObtenerTicketPorID(id);

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
            desestimacion.setTicket(ticketService.getObtenerTicketPorID(id));
            desestimacion.setUsuario(usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado()));
            desestimacion.setHora(desestimacion.getHora());
            desestimacion.setFecha(desestimacion.getFecha());
            atencionService.saveDesestimacion(desestimacion);

            Notificacion notificacion = new Notificacion();
            notificacion.setTicket(desestimacion.getTicket());
            notificacion.setHora(notificacion.getHora());
            notificacion.setFecha(notificacion.getFecha());
            notificacion.setAbierto(Boolean.FALSE);
            notificacion.setLeido(Boolean.FALSE);
            notificacion.setUsuario(desestimacion.getTicket().getUsuario());
            notificacion.setMensaje(" Ha sido Desestimado");
            notificacion.setUrl("/TicketsDesestimados");

            notificacionesService.saveNotiicacion(notificacion);
            WSNotificacionesService.enviarNotificacion(notificacion);
            WSNotificacionesService.enviarAtencionAVistaSoporteHistorialDesestimacion(desestimacion, ticketService);
            WSNotificacionesService.enviarAtencionAVistaUsuarioDesestimados(desestimacion, ticketService);
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
            System.out.println(e);
            // Captura otros errores
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer + "?error=desestimacion-general" : "/fallbackUrl?error=desestimacion-general");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al desestimar el ticket.");
        }

    }


}
