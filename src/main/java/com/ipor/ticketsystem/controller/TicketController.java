package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.dto.TicketDTO;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.model.fixed.ClasificacionIncidencia;
import com.ipor.ticketsystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/app/tickets")
public class TicketController {
    @Autowired
    TicketService ticketService;

    // Método para enviar Tickets y Datos Iniciales al Inicio, es llamado en WebController
    public Model retornaTicketsPropiosYDatosInicialesAInicio(Model model) {
        List<TicketDTO> MisTicketsDTO = ticketService.getMyTickets();
        model.addAttribute("MyTickets", MisTicketsDTO);
        List<TicketDTO> AllTicketsDTO = ticketService.getAllTickets();
        model.addAttribute("AllTickets", AllTicketsDTO);
        List<ClasificacionIncidencia> ListaTiposIncidencia = ticketService.getObtenerTodosLosTiposDeIncidencia();
        model.addAttribute("Lista_clasificacion_incidencia", ListaTiposIncidencia);

        return model;
    }

    @PostMapping("/crearTicket")
    public ResponseEntity<String> crearTicket(
            @RequestParam("descripcion") String descripcion,
            @RequestParam("clasificacion") Long clasificacion,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo) {

        // Lógica para crear el ticket (esto es un ejemplo)
        Ticket ticket = new Ticket();
        ticket.setDescripcion(descripcion);
        ClasificacionIncidencia clasificacionIncidencia = new ClasificacionIncidencia();
        clasificacionIncidencia = ticketService.ObtenerClasificacionIncidenciaPorID(clasificacion);
        ticket.setClasificacionIncidencia(clasificacionIncidencia);
        // Otras propiedades del ticket...
        ticketService.saveTicket(ticket);



        // Si el archivo no es nulo y no está vacío, guardarlo
        if (archivo != null && !archivo.isEmpty()) {
            try {
                ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
                archivoAdjunto.setNombre(archivo.getOriginalFilename());
                archivoAdjunto.setArchivo(archivo.getBytes());  // Convertir archivo a bytes
                archivoAdjunto.setTipoContenido(archivo.getContentType());
                archivoAdjunto.setPesoContenido((double) archivo.getSize() / 1024); // Tamaño en KB
                archivoAdjunto.setTicket(ticket);  // Asignar el ticket recién creado

                // Guardar el archivo en la base de datos
                ticketService.saveAdjunto(archivoAdjunto);

            } catch (IOException e) {
                System.out.println(ResponseEntity.badRequest().body("Error al procesar el archivo"));
                throw new RuntimeException(e);
            }
        }

        return ResponseEntity.ok("Ticket creado correctamente");
    }



}
