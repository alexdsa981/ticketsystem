package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.WebSocket.NotificationService;
import com.ipor.ticketsystem.model.dto.TicketDTO;
import com.ipor.ticketsystem.model.dto.TicketRecordWS;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.model.fixed.ClasificacionIncidencia;
import com.ipor.ticketsystem.service.TicketService;
import com.ipor.ticketsystem.service.UsuarioService;
import org.springframework.core.io.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/app/tickets")
public class TicketController {
    @Autowired
    TicketService ticketService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    SimpMessagingTemplate messagingTemplate;
    @Autowired
    NotificationService notificationService;

    // Método para enviar Tickets y Datos Iniciales al Inicio, es llamado en WebController
    public Model retornaTicketsPropiosAVista(Model model) {
        List<TicketDTO> MisTicketsDTO = ticketService.getMyTickets();
        Collections.reverse(MisTicketsDTO);
        model.addAttribute("MyTickets", MisTicketsDTO);
        return model;
    }

    public Model retornaListaClasificacionIncidencia(Model model) {
        List<ClasificacionIncidencia> ListaTiposIncidencia = ticketService.getObtenerTodosLosTiposDeIncidencia();
        model.addAttribute("Lista_clasificacion_incidencia", ListaTiposIncidencia);
        return model;
    }



    public Model retornaTicketRecibidosAVista(Model model) {
        List<TicketDTO> AllTicketsDTO = ticketService.getAllTicketsSinRecepcionar();
        Collections.reverse(AllTicketsDTO);
        model.addAttribute("AllTickets", AllTicketsDTO);
        return model;
    }

    @PostMapping("/crearTicket")
    public ResponseEntity<String> crearTicket(
            @RequestParam("descripcion") String descripcion,
            @RequestParam("clasificacion") Long clasificacion,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo,
            HttpServletResponse response) throws IOException {

        // Lógica para crear el ticket
        Ticket ticket = new Ticket();
        ticket.setDescripcion(descripcion);
        ClasificacionIncidencia clasificacionIncidencia = ticketService.getClasificacionIncidenciaPorID(clasificacion);
        ticket.setClasificacionIncidencia(clasificacionIncidencia);
        ticket.setFaseTicket(ticketService.getFaseTicketPorID(1L)); //enviado
        ticket.setUsuario(usuarioService.RetornarUsuarioPorId(usuarioService.RetornarIDdeUsuarioLogeado()));
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
        TicketDTO ticketDTO = new TicketDTO(ticket, listaArchivosAdjuntos);
        TicketRecordWS ticketRecordWS = new TicketRecordWS(ticketDTO);
        // Enviar el ticket creado a través de WebSocket
        messagingTemplate.convertAndSend("/topic/tickets", ticketRecordWS);

        String message = "Ticket Recibido: "+ticketRecordWS.nombreUsuario() + " - " + ticketRecordWS.nombreClasificacionIncidencia();
        notificationService.enviarNotificacion(message);


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


}
