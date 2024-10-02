package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.dto.TicketDTO;
import com.ipor.ticketsystem.model.fixed.ClasificacionIncidencia;
import com.ipor.ticketsystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

@Controller
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



}
