package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dto.TicketDTO;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;
import com.ipor.ticketsystem.model.dynamic.Ticket;
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
    public List<TicketDTO> getAllTicketsSinRecepcionar() {
        List<Ticket> Tickets = ticketRepository.findByFaseTicketId(1);
        List<TicketDTO> ListaTicketsDTO = new ArrayList<>();
        for (Ticket ticket : Tickets) {
            List<ArchivoAdjunto> adjuntosPorTicket = getArchivosAdjuntosDeTicketPorTicketID(ticket.getId());
            TicketDTO ticketDTO = new TicketDTO(ticket);
            ListaTicketsDTO.add(ticketDTO);
        }
        return ListaTicketsDTO;
    }


    // Método para obtener tickets propios enviados y en espera de aprobación de compra,
    // además junta los tickets y sus archivos adjuntos en TicketDTO
    public List<TicketDTO> getMyTickets() {
        Long idUsuario = usuarioService.getIDdeUsuarioLogeado();

        List<Ticket> MisTickets = ticketRepository.findByUsuarioIdAndFaseTicketId(idUsuario, 1L);

        // Crear una lista para almacenar los DTOs
        List<TicketDTO> MisTicketsDTO = new ArrayList<>();

        // Procesar los tickets de fase 1
        for (Ticket ticket : MisTickets) {
            List<ArchivoAdjunto> adjuntosPorTicket = getArchivosAdjuntosDeTicketPorTicketID(ticket.getId());
            TicketDTO ticketDTO = new TicketDTO(ticket);
            MisTicketsDTO.add(ticketDTO);
        }

        return MisTicketsDTO;
    }


    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    public void saveAdjunto(ArchivoAdjunto archivoAdjunto) {
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

    public Ticket getObtenerTicketPorID(Long id) {
        return ticketRepository.findById(id).get();
    }

    public void guardarTicketEnBaseDeDatos(Ticket ticket) {
        ticketRepository.save(ticket);
    }


    //obtener fase por id
    public FaseTicket getFaseTicketPorID(Long id) {
        return faseTicketRepository.findById(id).get();
    }



}
