package com.ipor.ticketsystem.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipor.ticketsystem.WebSocket.WSNotificacionesService;
import com.ipor.ticketsystem.model.dto.AtencionTicketDTO;
import com.ipor.ticketsystem.model.dynamic.*;
import com.ipor.ticketsystem.model.fixed.ClasificacionIncidencia;
import com.ipor.ticketsystem.repository.dynamic.TipoComponenteAdjuntoRepository;
import com.ipor.ticketsystem.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    TipoComponenteAdjuntoRepository tipoComponenteAdjuntoRepository;
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
        Collections.reverse(MyAtendidos);
        model.addAttribute("MyAtendidos", MyAtendidos);
        return model;
    }

    public Model getListaTodosLosTicketsAtendidosAVista(Model model) {
        List<AtencionTicketDTO> AllAtendidos = atencionService.getListaHistorialAtencion();
        Collections.reverse(AllAtendidos);
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
            @RequestParam("clasificacion") Long clasificacion,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        //cambiar fase de ticket
        atencionService.updateFaseTicket(id, 2L);
        // Lógica para crear la recepcion
        Recepcion recepcion = new Recepcion();
        recepcion.setMensaje(mensaje);
        recepcion.setClasificacionUrgencia(clasificadoresService.getClasificacionUrgenciaPorId(IDclasificacion_urgencia));
        recepcion.setTicket(ticketService.getObtenerTicketPorID(id));
        recepcion.setUsuario(usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado()));
        recepcion.setHora(recepcion.getHora());
        recepcion.setFecha(recepcion.getFecha());
        atencionService.saveRecepcion(recepcion);

        //Modifica el ticket para colocarle una clasificacion de incidencia
        ClasificacionIncidencia clasificacionIncidencia = clasificadoresService.getClasificacionIncidenciaPorID(clasificacion);
        recepcion.getTicket().setClasificacionIncidencia(clasificacionIncidencia);
        ticketService.saveTicket(recepcion.getTicket());

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
        WSNotificacionesService.aumentarNumeroNotificacion(recepcion.getTicket().getUsuario().getId());


        // Redirigir a la URL actual
        String referer = request.getHeader("Referer");
        response.sendRedirect(referer != null ? referer : "/fallbackUrl");
        return ResponseEntity.ok("Ticket recepcionado correctamente");
    }

    //metodo para atender un ticket, crea un atendido(servicio) y cambia la fase del ticket:
    @PostMapping("/atencion/{id}")
    public ResponseEntity<String> atenderTicket(
            @RequestParam("descripcion") String descripcion,
            @RequestParam("clasificacion_servicio") Long IDclasificacion_servicio,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        //cambiar fase de ticket
        atencionService.updateFaseTicket(id, 3L);
        // Lógica para crear la recepcion
        Servicio servicio = new Servicio();
        servicio.setDescripcion(descripcion);
        servicio.setClasificacionServicio(clasificadoresService.getClasificacionServicioPorId(IDclasificacion_servicio));
        servicio.setTicket(ticketService.getObtenerTicketPorID(id));
        servicio.setUsuario(usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado()));
        servicio.setHora(servicio.getHora());
        servicio.setFecha(servicio.getFecha());
        atencionService.saveServicio(servicio);

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
        WSNotificacionesService.aumentarNumeroNotificacion(servicio.getTicket().getUsuario().getId());

        // Redirigir a la URL actual
        String referer = request.getHeader("Referer");
        response.sendRedirect(referer != null ? referer : "/fallbackUrl");
        return ResponseEntity.ok("Ticket atendido correctamente");
    }

    // Método para desestimar un ticket, crea una desestimación y cambia la fase del ticket:
    @PostMapping("/desestimacion/{id}")
    public ResponseEntity<String> desestimarTicket(
            @RequestParam("mensaje") String mensaje,
            @RequestParam("clasificacion_desestimacion") Long IDclasificacion_desestimacion,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

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
        WSNotificacionesService.aumentarNumeroNotificacion(desestimacion.getTicket().getUsuario().getId());

        //eliminar los componentes adjuntos
        List<TipoComponenteAdjunto> componentesAdjuntos = tipoComponenteAdjuntoRepository.BuscarPorIdTicket(id);
        for (TipoComponenteAdjunto componente : componentesAdjuntos) {
            tipoComponenteAdjuntoRepository.delete(componente);
        }
        if (atencionService.findRecepcionByTicketID(id) != null) {
            atencionService.deleteRecepcion(atencionService.findRecepcionByTicketID(id));
        } else {
            System.out.println("no se ha encontrado recepcion");
        }

        // Redirigir a la URL actual
        String referer = request.getHeader("Referer");
        response.sendRedirect(referer != null ? referer : "/fallbackUrl");

        return ResponseEntity.ok("Ticket desestimado correctamente");
    }

    @PostMapping("/RedirigirADireccion/{id}")
    public ResponseEntity<String> redirigirTicketADireccion(
            @PathVariable Long id,
            @RequestBody Map<String, Object> datos) throws IOException {

        // Extraer listas de componentes y cantidades
        List<String> componentes = (List<String>) datos.get("componentes");
        List<String> cantidades = (List<String>) datos.get("cantidades");

        for (int i = 0; i < componentes.size(); i++) {
            Long componenteId = Long.valueOf(componentes.get(i));
            Integer cantidad = Integer.valueOf(cantidades.get(i));

            TipoComponenteAdjunto tipoComponenteAdjunto = new TipoComponenteAdjunto();
            tipoComponenteAdjunto.setTipoComponente(clasificadoresService.getTipoComponentePorId(componenteId));
            tipoComponenteAdjunto.setCantidad(cantidad);
            tipoComponenteAdjunto.setTicket(ticketService.getObtenerTicketPorID(id));
            tipoComponenteAdjunto.setAprobado(null);
            ticketService.saveComponenteAdjunto(tipoComponenteAdjunto);
        }

        Ticket ticket = ticketService.getObtenerTicketPorID(id);
        Notificacion notificacion = new Notificacion();
        notificacion.setTicket(ticket);
        notificacion.setHora(notificacion.getHora());
        notificacion.setFecha(notificacion.getFecha());
        notificacion.setAbierto(Boolean.FALSE);
        notificacion.setLeido(Boolean.FALSE);
        notificacion.setUsuario(ticket.getUsuario());
        notificacion.setMensaje(" En espera de aprobación por Dirección");
        notificacion.setUrl("/inicio");
        notificacionesService.saveNotiicacion(notificacion);

        List<Usuario> listaDireccion = usuarioService.ListaUsuariosPorRol(4L);
        for (Usuario direccion : listaDireccion) {
            Notificacion notificacionDireccion = new Notificacion();
            notificacionDireccion.setTicket(ticket);
            notificacionDireccion.setHora(notificacionDireccion.getHora());
            notificacionDireccion.setFecha(notificacionDireccion.getFecha());
            notificacionDireccion.setAbierto(Boolean.FALSE);
            notificacionDireccion.setLeido(Boolean.FALSE);
            notificacionDireccion.setUsuario(direccion);
            notificacionDireccion.setMensaje(ticket.getUsuario().getNombre() + " Ha Enviado un Ticket");
            notificacionDireccion.setUrl("/direccion/Recibidos");
            notificacionesService.saveNotiicacion(notificacionDireccion);
            WSNotificacionesService.aumentarNumeroNotificacion(direccion.getId());
        }

        // Cambiar fase del ticket
        atencionService.updateFaseTicket(id, 5L);

        return ResponseEntity.ok("Ticket redireccionado a dirección correctamente");
    }


    //metodo para recepcionar un ticket, crea un recepcionado y cambia la fase del ticket:
    @PostMapping("/recepcionDireccion/{id}")
    public ResponseEntity<Map<String, String>> recepcionarTicketDirección(
            @RequestParam("componentesSeleccionados") String componentesJson,
            @PathVariable Long id) throws IOException {

        //desaprueba todos los componentes adjuntos
        List<TipoComponenteAdjunto> listaComponentesTicket = ticketService.geComponentesAdjuntosDeTicketPorTicketID(id);
        for (TipoComponenteAdjunto componenteAdjunto : listaComponentesTicket) {
            componenteAdjunto.setAprobado(false);
            ticketService.saveComponenteAdjunto(componenteAdjunto);
        }
        //aprueba los componentes adjuntos seleccionados, el resto quedan desaprobados
        List<Long> componentesSeleccionados = new ObjectMapper().readValue(componentesJson, new TypeReference<List<Long>>() {
        });
        for (Long idcomponente : componentesSeleccionados) {
            System.out.println(idcomponente);
            TipoComponenteAdjunto componenteAdjuntoAprobado = ticketService.getComponenteAdjuntoPorId(idcomponente);
            componenteAdjuntoAprobado.setAprobado(true);
            ticketService.saveComponenteAdjunto(componenteAdjuntoAprobado);
        }
        //cambiar fase de ticket
        atencionService.updateFaseTicket(id, 1L);

        //avisar a usuario que direccion ha revisado la solicitud
        Ticket ticket = ticketService.getObtenerTicketPorID(id);
        Notificacion notificacion = new Notificacion();
        notificacion.setTicket(ticket);
        notificacion.setHora(notificacion.getHora());
        notificacion.setFecha(notificacion.getFecha());
        notificacion.setAbierto(Boolean.FALSE);
        notificacion.setLeido(Boolean.FALSE);
        notificacion.setUsuario(ticket.getUsuario());
        notificacion.setMensaje(" Dirección ha revisado la solicitud");
        notificacion.setUrl("/inicio");
        notificacionesService.saveNotiicacion(notificacion);
        WSNotificacionesService.aumentarNumeroNotificacion(ticket.getUsuario().getId());

        //avisar a soportes que direccion a revisado la solicitud
        //NOTIFICACION EN BASE DE DATOS
        List<Usuario> listaSoportes = usuarioService.ListaUsuariosPorRol(2L);
        for (Usuario soporte : listaSoportes){
            Notificacion notificacionS = new Notificacion();
            notificacionS.setTicket(ticket);
            notificacionS.setHora(notificacionS.getHora());
            notificacionS.setFecha(notificacionS.getFecha());
            notificacionS.setAbierto(Boolean.FALSE);
            notificacionS.setLeido(Boolean.FALSE);
            notificacionS.setUsuario(soporte);
            notificacionS.setMensaje(" Dirección ha revisado la solicitud");
            notificacionS.setUrl("/soporte/Recepcionar");
            notificacionesService.saveNotiicacion(notificacionS);
            //AUMENTA EL CONTADOR DE NOTIFICACIONES EN TIEMPO REAL A LOS DE SOPORTE
            WSNotificacionesService.aumentarNumeroNotificacion(soporte.getId());
        }



        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("redirectUrl", "/direccion/Recibidos");

        return ResponseEntity.ok(response);
    }


}
