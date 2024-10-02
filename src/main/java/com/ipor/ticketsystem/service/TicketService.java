package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dto.TicketDTO;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.model.fixed.ClasificacionIncidencia;
import com.ipor.ticketsystem.repository.dynamic.ArchivoAdjuntoRepository;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionIncidenciaRepository;
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
    @Autowired
    private ClasificacionIncidenciaRepository clasificacionIncidenciaRepository;

    // Método para obtener todos los tickets, además junta los tickets y sus archivos adjuntos en TicketDTO
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

    // Método para obtener tickets propios, además junta los tickets y sus archivos adjuntos en TicketDTO
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
    //obtener archivos adjuntos de un ticket por el id del ticket
    public List<ArchivoAdjunto> getArchivosAdjuntosDeTicketPorTicketID(Long TicketId) {
        return archivoAdjuntoRepository.BuscarPorIdTicket(TicketId);
    }

    //obtener todos los tipos de incidencia
    public List<ClasificacionIncidencia> getObtenerTodosLosTiposDeIncidencia(){
        return clasificacionIncidenciaRepository.findAll();
    }

    //obtener clasificacion incidencia por id
    public ClasificacionIncidencia ObtenerClasificacionIncidenciaPorID(Long id){
        return clasificacionIncidenciaRepository.findById(id).get();
    }
    //guardar ticket y archivo adjunto en el repositorio
    public void saveTicket(Ticket ticket){
        ticketRepository.save(ticket);
    }
    public void saveAdjunto(ArchivoAdjunto archivoAdjunto){
        archivoAdjuntoRepository.save(archivoAdjunto);
    }
}
