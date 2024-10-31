package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.dto.AtencionTicketDTO;
import com.ipor.ticketsystem.model.dynamic.Recepcion;
import com.ipor.ticketsystem.model.dynamic.Servicio;
import com.ipor.ticketsystem.model.fixed.ClasificacionServicio;
import com.ipor.ticketsystem.model.fixed.ClasificacionUrgencia;
import com.ipor.ticketsystem.service.AtencionService;
import com.ipor.ticketsystem.service.TicketService;
import com.ipor.ticketsystem.service.UsuarioService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/app/tickets")
public class AtencionController {
    @Autowired
    AtencionService atencionService;
    @Autowired
    TicketService ticketService;
    @Autowired
    UsuarioService usuarioService;

    //metodo para enviar todos los tickets en proceso a "enProceso"
    public Model retornaMisTicketsEnProcesoAVista(Model model) {
        List<AtencionTicketDTO> MyRecepcionados =  atencionService.getMyListaRecepcionados();
        Collections.reverse(MyRecepcionados);
        model.addAttribute("MyRecepcionados", MyRecepcionados);
        return  model;
    }
    public Model retornaTodosLosTicketsEnProcesoAVista(Model model) {
        List<AtencionTicketDTO> AllRecepcionados =  atencionService.getListaRecepcionados();
        Collections.reverse(AllRecepcionados);
        model.addAttribute("AllRecepcionados", AllRecepcionados);
        return  model;
    }

    //metodo para enviar todos los tickets atendidos a "atendidos"
    public Model retornaMisTicketsAtendidosAVista(Model model) {
        List<AtencionTicketDTO> MyAtendidos = atencionService.getMyListaAtendidos();
        Collections.reverse(MyAtendidos);
        model.addAttribute("MyAtendidos", MyAtendidos);
        return  model;
    }
    public Model retornaTodosLosTicketsAtendidosAVista(Model model) {
        List<AtencionTicketDTO> AllAtendidos = atencionService.getListaHistorialAtencion();
        Collections.reverse(AllAtendidos);
        model.addAttribute("AllAtendidos", AllAtendidos);
        return  model;
    }





    //metodo para enviar Lista de clasificaciones urgencia a Inicio
    public Model retornaListaClasificacionesUrgenciaActivos(Model model){
        List<ClasificacionUrgencia> listaUrgencias = atencionService.obtenerListaClasificacionUrgencia();
        model.addAttribute("Lista_clasificacion_urgencia", listaUrgencias);
        return  model;
    }
    //metodo para enviar Lista de clasificaciones urgencia a Inicio
    public Model retornaListaClasificacionesServicioActivos(Model model){
        List<ClasificacionServicio> listaServicios = atencionService.obtenerListaClasificacionServicio();
        model.addAttribute("Lista_clasificacion_servicio", listaServicios);
        return  model;
    }



    //metodo para recepcionar un ticket, crea un recepcionado y cambia la fase del ticket:
    @PostMapping("/recepcion/{id}")
    public ResponseEntity<String> recepcionarTicket(
            @RequestParam("mensaje") String mensaje,
            @RequestParam("clasificacion_urgencia") Long IDclasificacion_urgencia,
            @PathVariable Long id,
            HttpServletResponse response) throws IOException {

        //cambiar fase de ticket
        atencionService.updateFaseTicket(id, 2L);
        // Lógica para crear la recepcion
        Recepcion recepcion = new Recepcion();
        recepcion.setMensaje(mensaje);
        recepcion.setClasificacionUrgencia(atencionService.obtenerClasificacionUrgenciaPorId(IDclasificacion_urgencia));
        recepcion.setTicket(ticketService.getObtenerTicketPorID(id));
        recepcion.setUsuario(usuarioService.RetornarUsuarioPorId(usuarioService.RetornarIDdeUsuarioLogeado()));
        recepcion.setHora(recepcion.getHora());
        recepcion.setFecha(recepcion.getFecha());
        atencionService.saveRecepcion(recepcion);

        response.sendRedirect("/soporte/Recibidos");
        return ResponseEntity.ok("Ticket recepcionado correctamente");
    }

    //metodo para recepcionar un ticket, crea un recepcionado y cambia la fase del ticket:
    @PostMapping("/atencion/{id}")
    public ResponseEntity<String> atenderTicket(
            @RequestParam("descripcion") String descripcion,
            @RequestParam("clasificacion_servicio") Long IDclasificacion_servicio,
            @PathVariable Long id,
            HttpServletResponse response) throws IOException {

        //cambiar fase de ticket
        atencionService.updateFaseTicket(id, 3L);
        // Lógica para crear la recepcion
        Servicio servicio = new Servicio();
        servicio.setDescripcion(descripcion);
        servicio.setClasificacionServicio(atencionService.obtenerClasificacionServicioPorId(IDclasificacion_servicio));
        servicio.setTicket(ticketService.getObtenerTicketPorID(id));
        servicio.setUsuario(usuarioService.RetornarUsuarioPorId(usuarioService.RetornarIDdeUsuarioLogeado()));
        servicio.setHora(servicio.getHora());
        servicio.setFecha(servicio.getFecha());
        atencionService.saveServicio(servicio);

        response.sendRedirect("/soporte/Recepcionados");
        return ResponseEntity.ok("Ticket atendido correctamente");
    }

}
