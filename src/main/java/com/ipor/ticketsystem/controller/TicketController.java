package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.WebSocket.WSNotificacionesService;
import com.ipor.ticketsystem.model.dto.TicketDTO;
import com.ipor.ticketsystem.model.dynamic.*;
import com.ipor.ticketsystem.service.NotificacionesService;
import com.ipor.ticketsystem.service.TicketService;
import com.ipor.ticketsystem.service.UsuarioService;
import org.springframework.core.io.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/app/tickets")
public class TicketController {
    @Autowired
    TicketService ticketService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    WSNotificacionesService WSNotificacionesService;
    @Autowired
    NotificacionesService notificacionesService;


    //ESTA CLASE REPRESENTA LA LOGICA PARA MOSTRAR TICKETS CREADOS/REDIRIGIDOS (AÚN SIN RECEPCIONAR) EN LAS VISTAS

    public Model retornaTicketsPropiosAVista(Model model) {
        List<TicketDTO> MisTicketsDTO = ticketService.getMyTickets();
        Collections.reverse(MisTicketsDTO);
        model.addAttribute("MyTickets", MisTicketsDTO);
        return model;
    }

    public Model retornaTicketsRecibidosAVista(Model model) {
        List<TicketDTO> AllTicketsDTO = ticketService.getAllTicketsSinRecepcionar();
        Collections.reverse(AllTicketsDTO);
        model.addAttribute("AllTickets", AllTicketsDTO);
        return model;
    }
    public Model retornaTicketsRecibidosAVistaDireccion(Model model) {
        List<TicketDTO> AllTicketsDTO = ticketService.getAllTicketsSinRecepcionarDireccion();
        Collections.reverse(AllTicketsDTO);
        model.addAttribute("AllTickets", AllTicketsDTO);
        return model;
    }
    public Model getListaTodosLosTicketsRevisadosPorDireccionAVista(Model model) {
        List<TicketDTO> AllRevisadosDireccion = ticketService.getListaRevisadosDireccion();
        Collections.reverse(AllRevisadosDireccion);
        model.addAttribute("AllRevisadosDireccion", AllRevisadosDireccion);
        return model;
    }

    //crea ticket y además lo envia a través de webSocket para notificaciones y actualizaciones en tiempo real
    @PostMapping("/crearTicket")
    public ResponseEntity<String> crearTicket(
            @RequestParam("descripcion") String descripcion,
//            @RequestParam("clasificacion") Long clasificacion,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo,
            HttpServletResponse response) throws IOException {

        // Lógica para crear el ticket
        Ticket ticket = new Ticket();
        ticket.setDescripcion(descripcion);
        ticket.setFaseTicket(ticketService.getFaseTicketPorID(1L)); //enviado
        ticket.setUsuario(usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado()));
        ticket.setFecha(ticket.getFecha());
        ticket.setHora(ticket.getHora());

        ticketService.saveTicket(ticket);


        List<ArchivoAdjunto> listaArchivosAdjuntos = new ArrayList<>();
        // Si el archivo no es nulo y no está vacío, guardarlo
        if (archivo != null && !archivo.isEmpty()) {
            try {
                ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
                archivoAdjunto.setNombre(archivo.getOriginalFilename());
                archivoAdjunto.setArchivo(archivo.getBytes());  // Convertir archivo a bytes
                archivoAdjunto.setTipoContenido(archivo.getContentType());
                archivoAdjunto.setPesoContenido((double) archivo.getSize() / 1024); // Tamaño en KB
                archivoAdjunto.setTicket(ticket);  // Asignar el ticket recién creado
                listaArchivosAdjuntos.add(archivoAdjunto);
                // Guardar el archivo en la base de datos
                ticketService.saveAdjunto(archivoAdjunto);

            } catch (IOException e) {
                ResponseEntity.badRequest().body("Error al procesar el archivo");
                System.out.println(e.getMessage());
                System.out.println(Arrays.toString(e.getStackTrace()));
                throw new RuntimeException(e);
            }

        }


        //NOTIFICACION EN BASE DE DATOS
        List<Usuario> listaSoportes = usuarioService.ListaUsuariosPorRol(2L);
        for (Usuario soporte : listaSoportes){
            Notificacion notificacion = new Notificacion();
            notificacion.setTicket(ticket);
            notificacion.setHora(notificacion.getHora());
            notificacion.setFecha(notificacion.getFecha());
            notificacion.setAbierto(Boolean.FALSE);
            notificacion.setLeido(Boolean.FALSE);
            notificacion.setUsuario(soporte);
            notificacion.setMensaje(ticket.getUsuario().getNombre() + " Ha Enviado un Ticket");
            notificacion.setUrl("/soporte/Recepcionar");
            notificacionesService.saveNotiicacion(notificacion);
            //AUMENTA EL CONTADOR DE NOTIFICACIONES EN TIEMPO REAL A LOS DE SOPORTE
            WSNotificacionesService.enviarNotificacion(notificacion);
        }

        //NOTIFICACIONES EN TIEMPO REAL A TRAVES DE WEB SOCKETS
        TicketDTO ticketDTO = new TicketDTO(ticket);
        WSNotificacionesService.enviarTicketAVistaSoporteRecepcion(ticketDTO);


        response.sendRedirect("/inicio");
        return ResponseEntity.ok("Ticket creado correctamente");
    }


    @GetMapping("/adjunto/descargar/{id}")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable Long id) {
        // Obtener el archivo adjunto por su ID desde el servicio
        ArchivoAdjunto archivoAdjunto = ticketService.getArchivoPorId(id);

        // Crear un recurso basado en los bytes del archivo
        Resource recurso = new ByteArrayResource(archivoAdjunto.getArchivo());

        // Devolver el archivo como respuesta, con el tipo de contenido adecuado
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(archivoAdjunto.getTipoContenido())) // Tipo MIME
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivoAdjunto.getNombre() + "\"") // Para descarga
                .body(recurso); // El cuerpo de la respuesta es el recurso
    }

    @GetMapping("/componentes/{id}")
    public ResponseEntity<List<TipoComponenteAdjunto>> getComponentesPorTicketID(@PathVariable Long id) {
        List<TipoComponenteAdjunto> listaComponentes = ticketService.geComponentesAdjuntosDeTicketPorTicketID(id);
        return ResponseEntity.ok(listaComponentes);
    }


}
