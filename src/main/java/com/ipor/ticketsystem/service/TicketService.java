package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dto.TicketDTO;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.repository.dynamic.ArchivoAdjuntoRepository;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {


    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ArchivoAdjuntoRepository archivoAdjuntoRepository;
    @Autowired
    private WebService webService;

    // Método para obtener todos los tickets
    public List<TicketDTO> getAllTickets() {
        List<Ticket> Tickets = ticketRepository.findAll();
        List<TicketDTO> MisTicketsDTO = new ArrayList<>();
        for (Ticket ticket : Tickets) {
            List<ArchivoAdjunto> adjuntosPorTicket = getArchivosAdjuntosDeTicketPorTicketID(ticket.getId());
            TicketDTO ticketDTO = new TicketDTO(ticket, adjuntosPorTicket);
            MisTicketsDTO.add(ticketDTO);
        }
        return MisTicketsDTO;
    }

    // Método para obtener tickets propios
    public List<TicketDTO> getMyTickets() {
        Long idUsuario = webService.RetornarIDdeUsuario();
        List<Ticket> MisTickets = ticketRepository.findByUsuarioIdAndFaseTicketId(idUsuario, 1L);
        List<TicketDTO> MisTicketsDTO = new ArrayList<>();
        for (Ticket ticket : MisTickets) {
            List<ArchivoAdjunto> adjuntosPorTicket = getArchivosAdjuntosDeTicketPorTicketID(ticket.getId());
            TicketDTO ticketDTO = new TicketDTO(ticket, adjuntosPorTicket);
            MisTicketsDTO.add(ticketDTO);
        }

        return MisTicketsDTO;
    }

    public List<ArchivoAdjunto> getArchivosAdjuntosDeTicketPorTicketID(Long TicketId) {
        return archivoAdjuntoRepository.BuscarPorIdTicket(TicketId);
    }

}
