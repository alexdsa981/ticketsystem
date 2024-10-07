package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dto.AtencionTicketDTO;
import com.ipor.ticketsystem.model.dynamic.Recepcion;
import com.ipor.ticketsystem.model.dynamic.Servicio;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.model.fixed.ClasificacionServicio;
import com.ipor.ticketsystem.model.fixed.ClasificacionUrgencia;
import com.ipor.ticketsystem.model.fixed.FaseTicket;
import com.ipor.ticketsystem.repository.dynamic.RecepcionRepository;
import com.ipor.ticketsystem.repository.dynamic.ServicioRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionServicioRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionUrgenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AtencionService {
    @Autowired
    RecepcionRepository recepcionRepository;
    @Autowired
    ServicioRepository servicioRepository;
    @Autowired
    TicketService ticketService;
    @Autowired
    ClasificacionUrgenciaRepository clasificacionUrgenciaRepository;
    @Autowired
    ClasificacionServicioRepository clasificacionServicioRepository;
    @Autowired
    UsuarioService usuarioService;

    //metodo para retornar todos los tickets en proceso:
    public List<AtencionTicketDTO> getListaRecepcionados() {
        List<Recepcion> listaRecepcionados = recepcionRepository.findAll();
        List<AtencionTicketDTO> listaRecepcionadosDTO = new ArrayList<>();
        for (Recepcion recepcion : listaRecepcionados) {
            AtencionTicketDTO recepcionadoDTO = new AtencionTicketDTO(recepcion);
            listaRecepcionadosDTO.add(recepcionadoDTO);
        }
        return listaRecepcionadosDTO;
    }

    //metodo para retornar Mis tickets en proceso:
    public List<AtencionTicketDTO> getMyListaRecepcionados() {
        List<Recepcion> listaRecepcionados = recepcionRepository.findAllByUsuarioId(usuarioService.RetornarIDdeUsuarioLogeado());
        List<AtencionTicketDTO> listaRecepcionadosDTO = new ArrayList<>();
        for (Recepcion recepcion : listaRecepcionados) {
            AtencionTicketDTO recepcionadoDTO = new AtencionTicketDTO(recepcion);
            listaRecepcionadosDTO.add(recepcionadoDTO);
        }
        return listaRecepcionadosDTO;
    }

    //metodo para retornar todos los tickets atendidos:
    public List<AtencionTicketDTO> getListaAtendidos() {
        List<Servicio> listaAtendidos = servicioRepository.findAll();
        List<AtencionTicketDTO> listaAtendidosDTO = new ArrayList<>();
        for (Servicio servicio : listaAtendidos) {
            AtencionTicketDTO recepcionadoDTO = new AtencionTicketDTO(servicio);
            listaAtendidosDTO.add(recepcionadoDTO);
        }
        return listaAtendidosDTO;
    }

    //cambiar fase de ticket para recepcion o atención
    public void updateFaseTicket(Long idTicket, Long idFaseTicket) {
        // Buscar el ticket existente por ID
        Ticket ticketExistente = ticketService.getObtenerTicketPorID(idTicket);
        ticketExistente.setFaseTicket(ticketService.getFaseTicketPorID(2L));
        ticketService.guardarTicketEnBaseDeDatos(ticketExistente);
    }


    //obtener clasificacion urgencia por id
    public ClasificacionUrgencia obtenerClasificacionUrgenciaPorId(Long id){
        return clasificacionUrgenciaRepository.findById(id).get();
    }
    //obtener clasificaciones urgencia
    public List<ClasificacionUrgencia> obtenerListaClasificacionUrgencia(){
        return clasificacionUrgenciaRepository.findAll();
    }

    //obtener clasificacion servicio por id
    public ClasificacionServicio obtenerClasificacionServicioPorId(Long id){
        return clasificacionServicioRepository.findById(id).get();
    }
    //obtener clasificaciones servicio
    public List<ClasificacionServicio> obtenerListaClasificacionServicio(){
        return clasificacionServicioRepository.findAll();
    }

    //guardar recepcion en base de datos
    public void saveRecepcion(Recepcion recepcion){
        recepcionRepository.save(recepcion);
    }
    //guardar servicio en base de datos
    public void saveServicio(Servicio servicio){
        servicioRepository.save(servicio);
    }

}
