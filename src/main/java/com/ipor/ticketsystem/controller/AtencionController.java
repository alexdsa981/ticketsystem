package com.ipor.ticketsystem.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipor.ticketsystem.model.dto.AtencionTicketDTO;
import com.ipor.ticketsystem.model.dynamic.Desestimacion;
import com.ipor.ticketsystem.model.dynamic.Recepcion;
import com.ipor.ticketsystem.model.dynamic.Servicio;
import com.ipor.ticketsystem.model.dynamic.TipoComponenteAdjunto;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    //ESTA CLASE REPRESENTA LA LOGICA PARA MOSTRAR TICKETS DESDE LA FASE RECEPCIÓN
    // ATENDIDO O DESESTIMADO EN LAS VISTAS

    public Model getListaMisTicketsRecepcionadosAVista(Model model) {
        List<AtencionTicketDTO> MyRecepcionados =  atencionService.getMyListaRecepcionados();
        Collections.reverse(MyRecepcionados);
        model.addAttribute("MyRecepcionados", MyRecepcionados);
        return  model;
    }
    public Model getListaTodosLosTicketsRecepcionadosAVista(Model model) {
        List<AtencionTicketDTO> AllRecepcionados =  atencionService.getListaRecepcionados();
        model.addAttribute("AllRecepcionados", AllRecepcionados);
        return  model;
    }
    public Model getListaTodosLosTicketsRecepcionadosPorDireccionAVista(Model model) {
        List<AtencionTicketDTO> AllRecepcionadosDireccion =  atencionService.getListaRecepcionadosDireccion();
        model.addAttribute("AllRecepcionadosDireccion", AllRecepcionadosDireccion);
        return  model;
    }

    public Model getListaMisTicketsAtendidosAVista(Model model) {
        List<AtencionTicketDTO> MyAtendidos = atencionService.getMyListaAtendidos();
        Collections.reverse(MyAtendidos);
        model.addAttribute("MyAtendidos", MyAtendidos);
        return  model;
    }
    public Model getListaTodosLosTicketsAtendidosAVista(Model model) {
        List<AtencionTicketDTO> AllAtendidos = atencionService.getListaHistorialAtencion();
        Collections.reverse(AllAtendidos);
        model.addAttribute("AllAtendidos", AllAtendidos);
        return  model;
    }
    public Model getListaMisTicketsDesestimadosAVista(Model model) {
        List<AtencionTicketDTO> MyDesestimados = atencionService.getMyListaDesestimados();
        Collections.reverse(MyDesestimados);
        model.addAttribute("MyDesestimados", MyDesestimados);
        return  model;
    }
    public Model getListaTodosLosTicketsDesestimadosAVista(Model model) {
        List<AtencionTicketDTO> AllDesestimados = atencionService.getListaDesestimados();
        Collections.reverse(AllDesestimados);
        model.addAttribute("AllDesestimados", AllDesestimados);
        return  model;
    }



    //metodo para recepcionar un ticket(desde soporte), crea un recepcionado y cambia la fase del ticket:
    @PostMapping("/recepcion/{id}")
    public ResponseEntity<String> recepcionarTicket(
            @RequestParam("mensaje") String mensaje,
            @RequestParam("clasificacion_urgencia") Long IDclasificacion_urgencia,
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

        //eliminar los componentes adjuntos
        List<TipoComponenteAdjunto> componentesAdjuntos = tipoComponenteAdjuntoRepository.BuscarPorIdTicket(id);
        for(TipoComponenteAdjunto componente : componentesAdjuntos){
            tipoComponenteAdjuntoRepository.delete(componente);
        }
        if (atencionService.findRecepcionByTicketID(id) != null){
            atencionService.deleteRecepcion(atencionService.findRecepcionByTicketID(id));
        }else{
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

        // Cambiar fase del ticket
        atencionService.updateFaseTicket(id, 5L);

        return ResponseEntity.ok("Ticket redireccionado a dirección correctamente");
    }


    //metodo para recepcionar un ticket, crea un recepcionado y cambia la fase del ticket:
    @PostMapping("/recepcionDireccion/{id}")
    public ResponseEntity<String> recepcionarTicketDirección(
            @RequestParam("mensaje") String mensaje,
            @RequestParam("clasificacion_urgencia") Long IDclasificacion_urgencia,
            @RequestParam("componentesSeleccionados") String componentesJson,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        //desaprueba todos los componentes adjuntos
        List<TipoComponenteAdjunto> listaComponentesTicket = ticketService.geComponentesAdjuntosDeTicketPorTicketID(id);
        for (TipoComponenteAdjunto componenteAdjunto : listaComponentesTicket){
            componenteAdjunto.setAprobado(false);
            ticketService.saveComponenteAdjunto(componenteAdjunto);
        }
        //aprueba los componentes adjuntos seleccionados, el resto quedan desaprobados
        List<Long> componentesSeleccionados = new ObjectMapper().readValue(componentesJson, new TypeReference<List<Long>>() {});
        for (Long idcomponente : componentesSeleccionados){
            System.out.println(idcomponente);
            new TipoComponenteAdjunto();
            TipoComponenteAdjunto componenteAdjuntoAprobado;
            componenteAdjuntoAprobado = ticketService.getComponenteAdjuntoPorId(idcomponente);
            componenteAdjuntoAprobado.setAprobado(true);
            ticketService.saveComponenteAdjunto(componenteAdjuntoAprobado);
        }
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

        response.sendRedirect("/direccion/Recibidos");
        return ResponseEntity.ok("Ticket recepcionado correctamente");
    }



}
