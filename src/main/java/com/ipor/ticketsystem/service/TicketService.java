package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {


    @Autowired
    private TicketRepository ticketRepository;

    // Método para obtener todos los tickets
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

}
