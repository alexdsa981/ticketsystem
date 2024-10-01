package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class TicketController {
    @Autowired
    TicketService ticketService;

    // Método para agregar los tickets al modelo
    public Model retornaTicketsPropiosAInicio(Model model) {
        return model.addAttribute("MyTickets", ticketService.getMyTickets());
    }


}
