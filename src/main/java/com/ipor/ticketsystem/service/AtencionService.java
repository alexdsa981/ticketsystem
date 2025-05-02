package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dto.DetalleTicketDTO;
import com.ipor.ticketsystem.model.dynamic.*;
import com.ipor.ticketsystem.repository.dynamic.DesestimacionRepository;
import com.ipor.ticketsystem.repository.dynamic.RecepcionRepository;
import com.ipor.ticketsystem.repository.dynamic.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AtencionService {
    @Autowired
    RecepcionRepository recepcionRepository;
    @Autowired
    ServicioRepository servicioRepository;
    @Autowired
    DesestimacionRepository desestimacionRepository;
    @Autowired
    TicketService ticketService;
    @Autowired
    UsuarioService usuarioService;

    //metodo para retornar todos los tickets en proceso:
    public List<DetalleTicketDTO> getListaRecepcionados() {
        List<Recepcion> listaRecepcionados = recepcionRepository.findAllByTicketFaseID2();
        List<DetalleTicketDTO> listaRecepcionadosDTO = new ArrayList<>();
        for (Recepcion recepcion : listaRecepcionados) {
            DetalleTicketDTO recepcionadoDTO = new DetalleTicketDTO(recepcion.getTicket());
            listaRecepcionadosDTO.add(recepcionadoDTO);
        }
        return listaRecepcionadosDTO;
    }

    //metodo para retornar Mis tickets en proceso:
    public List<DetalleTicketDTO> getMyListaRecepcionados() {
        List<Recepcion> listaRecepcionados = recepcionRepository.findAllByTicketUsuarioId(usuarioService.getIDdeUsuarioLogeado());
        List<DetalleTicketDTO> listaRecepcionadosDTO = new ArrayList<>();
        for (Recepcion recepcion : listaRecepcionados) {
            DetalleTicketDTO recepcionadoDTO = new DetalleTicketDTO(recepcion.getTicket());
            listaRecepcionadosDTO.add(recepcionadoDTO);
        }
        return listaRecepcionadosDTO;
    }


    //metodo para retornar todos los tickets desestimados:
    public List<DetalleTicketDTO> getListaDesestimados() {
        List<Desestimacion> listaDesestimados = desestimacionRepository.findAllByTicketFaseID4();
        List<DetalleTicketDTO> listaDesestimadosDTO = new ArrayList<>();
        for (Desestimacion desestimacion : listaDesestimados) {
            DetalleTicketDTO desestimadoDTO = new DetalleTicketDTO(desestimacion.getTicket());
            listaDesestimadosDTO.add(desestimadoDTO);
        }
        return listaDesestimadosDTO;
    }
    //metodo para retornar Mis tickets desestimados:
    public List<DetalleTicketDTO> getMyListaDesestimados() {
        List<Desestimacion> listaDesestimaciones = desestimacionRepository.findAllByTicketUsuarioId(usuarioService.getIDdeUsuarioLogeado());
        List<DetalleTicketDTO> listaDesestimacionesDTO = new ArrayList<>();
        for (Desestimacion desestimacion : listaDesestimaciones) {
            DetalleTicketDTO desestimacionDTO = new DetalleTicketDTO(desestimacion.getTicket());
            listaDesestimacionesDTO.add(desestimacionDTO);
        }
        return listaDesestimacionesDTO;
    }
    //metodo para retornar Mis tickets atendidos:
    public List<DetalleTicketDTO> getMyListaAtendidos() {
        List<Servicio> listaServicios = servicioRepository.findAllByTicketUsuarioId(usuarioService.getIDdeUsuarioLogeado());
        List<DetalleTicketDTO> listaServiciosDTO = new ArrayList<>();
        for (Servicio servicio : listaServicios) {
            DetalleTicketDTO servicioDTO = new DetalleTicketDTO(servicio.getTicket());
            listaServiciosDTO.add(servicioDTO);
        }
        return listaServiciosDTO;
    }

    //metodo para retornar historial de atencion:
    public List<DetalleTicketDTO> getListaHistorialAtencion() {
        List<Servicio> listaAtendidos = servicioRepository.findAllByTicketFaseID3();
        List<DetalleTicketDTO> listaServiciosDTO = new ArrayList<>();

        for (Servicio servicio : listaAtendidos) {
            Recepcion recepcion = recepcionRepository.findByTicketId(servicio.getTicket().getId());
            DetalleTicketDTO servicioDTO = new DetalleTicketDTO(servicio.getTicket());
            listaServiciosDTO.add(servicioDTO);
        }
        return listaServiciosDTO;
    }

    //cambiar fase de ticket para recepcion o atención
    public void updateFaseTicket(Long idTicket, Long idFaseTicket) {
        // Buscar el ticket existente por ID
        Ticket ticketExistente = ticketService.getTicketPorID(idTicket);
        ticketExistente.setFaseTicket(ticketService.getFaseTicketPorID(idFaseTicket));
        ticketService.guardarTicketEnBaseDeDatos(ticketExistente);
    }





    //guardar recepcion en base de datos
    public void saveRecepcion(Recepcion recepcion){
        recepcionRepository.save(recepcion);
    }
    //guardar servicio en base de datos
    public void saveServicio(Servicio servicio){
        servicioRepository.save(servicio);
    }
    //guardar servicio en base de datos
    public void saveDesestimacion(Desestimacion desestimacion){
        desestimacionRepository.save(desestimacion);
    }

    public void deleteRecepcion(Recepcion recepcion){
        recepcionRepository.deleteByTicketId(recepcion.getTicket().getId());
    }
    public Recepcion findRecepcionByTicketID(Long TicketID){
        return recepcionRepository.findByTicketId(TicketID);
    }

}
