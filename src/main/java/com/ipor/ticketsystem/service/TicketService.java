package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dto.TicketDTO;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.model.fixed.ClasificacionIncidencia;
import com.ipor.ticketsystem.model.fixed.FaseTicket;
import com.ipor.ticketsystem.repository.dynamic.ArchivoAdjuntoRepository;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionIncidenciaRepository;
import com.ipor.ticketsystem.repository.fixed.FaseTicketRepository;
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
    private UsuarioService usuarioService;
    @Autowired
    private ClasificacionIncidenciaRepository clasificacionIncidenciaRepository;
    @Autowired
    private FaseTicketRepository faseTicketRepository;
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
        Long idUsuario = usuarioService.RetornarIDdeUsuario();
        List<Ticket> MisTickets = ticketRepository.findByUsuarioIdAndFaseTicketId(idUsuario, 1L);
        List<TicketDTO> MisTicketsDTO = new ArrayList<>();
        for (Ticket ticket : MisTickets) {
            List<ArchivoAdjunto> adjuntosPorTicket = getArchivosAdjuntosDeTicketPorTicketID(ticket.getId());
            TicketDTO ticketDTO = new TicketDTO(ticket, adjuntosPorTicket);
            MisTicketsDTO.add(ticketDTO);
        }

        return MisTicketsDTO;
    }

    //guardar ticket y archivo adjunto en el repositorio
    public void saveTicket(Ticket ticket){
        ticketRepository.save(ticket);
    }
    public void saveAdjunto(ArchivoAdjunto archivoAdjunto){
        archivoAdjuntoRepository.save(archivoAdjunto);
    }

    //obtener archivos adjuntos de un ticket por el id del ticket
    public List<ArchivoAdjunto> getArchivosAdjuntosDeTicketPorTicketID(Long TicketId) {
        return archivoAdjuntoRepository.BuscarPorIdTicket(TicketId);
    }
    //obtener archivo adjunto por ID
    public ArchivoAdjunto getArchivoPorId(Long id) {
        return archivoAdjuntoRepository.findById(id)
                .get();
    }



    //obtener todos los tipos de incidencia
    public List<ClasificacionIncidencia> getObtenerTodosLosTiposDeIncidencia(){
        return clasificacionIncidenciaRepository.findAll();
    }

    //obtener clasificacion incidencia por id
    public ClasificacionIncidencia getClasificacionIncidenciaPorID(Long id){
        return clasificacionIncidenciaRepository.findById(id).get();
    }

    //obtener fase por id
    public FaseTicket getFaseTicketPorID(Long id){
        return faseTicketRepository.findById(id).get();
    }

}
