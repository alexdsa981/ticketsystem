package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.dto.TicketDTO;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TicketController {
    @Autowired
    TicketService ticketService;

    // Método para agregar los tickets al modelo
    public Model retornaTicketsPropiosAInicio(Model model) {
        List<TicketDTO> MisTicketsDTO = ticketService.getMyTickets();
        model.addAttribute("MyTickets", MisTicketsDTO);
        List<TicketDTO> AllTicketsDTO = ticketService.getAllTickets();
        model.addAttribute("AllTickets", AllTicketsDTO);
        return model;
    }

    //Metodo para llamar archivo adjunto


}
