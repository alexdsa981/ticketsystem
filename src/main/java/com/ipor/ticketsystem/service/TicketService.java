package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dto.DetalleTicketDTO;
import com.ipor.ticketsystem.model.dynamic.*;
import com.ipor.ticketsystem.model.fixed.FaseTicket;
import com.ipor.ticketsystem.repository.dynamic.*;
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
    private ArchivoAdjuntoEnvioRepository archivoAdjuntoEnvioRepository;
    @Autowired
    private ArchivoAdjuntoAtencionRepository archivoAdjuntoAtencionRepository;
    @Autowired
    private ArchivoAdjuntoDesestimacionRepository archivoAdjuntoDesestimacionRepository;
    @Autowired
    private ArchivoAdjuntoEsperaRepository archivoAdjuntoEsperaRepository;
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

    public void saveAdjuntoEnvio(ArchivoAdjuntoEnvio archivoAdjuntoEnvio) {
        archivoAdjuntoEnvioRepository.save(archivoAdjuntoEnvio);
    }
    public void saveAdjuntoAtencion(ArchivoAdjuntoAtencion archivoAdjuntoAtencion) {
        archivoAdjuntoAtencionRepository.save(archivoAdjuntoAtencion);
    }
    public void saveAdjuntoDesestimacion(ArchivoAdjuntoDesestimacion archivoAdjuntoDesestimacion) {
        archivoAdjuntoDesestimacionRepository.save(archivoAdjuntoDesestimacion);
    }

    public void saveAdjuntoEspera(ArchivoAdjuntoEspera archivoAdjuntoEspera) {
        archivoAdjuntoEsperaRepository.save(archivoAdjuntoEspera);
    }

    //obtener archivo adjunto por ID
    public ArchivoAdjuntoEnvio getArchivoEnvioPorId(Long id) {
        return archivoAdjuntoEnvioRepository.findById(id)
                .get();
    }
    public ArchivoAdjuntoAtencion getArchivoAtencionPorId(Long id) {
        return archivoAdjuntoAtencionRepository.findById(id)
                .get();
    }
    public ArchivoAdjuntoDesestimacion getArchivoDesestimacionPorId(Long id) {
        return archivoAdjuntoDesestimacionRepository.findById(id)
                .get();
    }
    public ArchivoAdjuntoEspera getArchivoEsperaPorId(Long id) {
        return archivoAdjuntoEsperaRepository.findById(id)
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
