package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dto.DetalleTicketDTO;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.model.fixed.FaseTicket;
import com.ipor.ticketsystem.repository.dynamic.ArchivoAdjuntoRepository;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import com.ipor.ticketsystem.repository.fixed.TipoIncidenciaRepository;
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
    private TipoIncidenciaRepository tipoIncidenciaRepository;
    @Autowired
    private FaseTicketRepository faseTicketRepository;

    // Método para obtener todos los tickets, además junta los tickets y sus archivos adjuntos en TicketDTO
    public List<DetalleTicketDTO> getAllTicketsSinRecepcionar() {
        List<Ticket> Tickets = ticketRepository.findAllSinRecepcionar();
        List<DetalleTicketDTO> ListaTicketsDTO = new ArrayList<>();
        for (Ticket ticket : Tickets) {
            DetalleTicketDTO ticketDTO = new DetalleTicketDTO(ticket);
            ListaTicketsDTO.add(ticketDTO);
        }
        return ListaTicketsDTO;
    }


    // Método para obtener tickets propios enviados y en espera de aprobación de compra,
    // además junta los tickets y sus archivos adjuntos en TicketDTO
    public List<DetalleTicketDTO> getMyTickets() {
        Long idUsuario = usuarioService.getIDdeUsuarioLogeado();

        List<Ticket> MisTickets = ticketRepository.findAllByTicketUsuarioId(idUsuario);

        // Crear una lista para almacenar los DTOs
        List<DetalleTicketDTO> MisTicketsDTO = new ArrayList<>();

        // Procesar los tickets de fase 1
        for (Ticket ticket : MisTickets) {
            DetalleTicketDTO ticketDTO = new DetalleTicketDTO(ticket);
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

    public Ticket getTicketPorID(Long id) {
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
