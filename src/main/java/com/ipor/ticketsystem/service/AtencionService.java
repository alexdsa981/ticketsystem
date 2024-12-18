package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dto.AtencionTicketDTO;
import com.ipor.ticketsystem.model.dynamic.*;
import com.ipor.ticketsystem.repository.dynamic.DesestimacionRepository;
import com.ipor.ticketsystem.repository.dynamic.RecepcionRepository;
import com.ipor.ticketsystem.repository.dynamic.ServicioRepository;
import com.ipor.ticketsystem.repository.dynamic.TipoComponenteAdjuntoRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionDesestimacionRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionServicioRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionUrgenciaRepository;
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
    public List<AtencionTicketDTO> getListaRecepcionados() {
        List<Recepcion> listaRecepcionados = recepcionRepository.findAllByTicketFaseID2();
        List<AtencionTicketDTO> listaRecepcionadosDTO = new ArrayList<>();
        for (Recepcion recepcion : listaRecepcionados) {
            AtencionTicketDTO recepcionadoDTO = new AtencionTicketDTO(recepcion, ticketService);
            listaRecepcionadosDTO.add(recepcionadoDTO);
        }
        return listaRecepcionadosDTO;
    }
    //metodo para retornar todos los tickets en proceso en Direccion:
    public List<AtencionTicketDTO> getListaRecepcionadosDireccion() {
        List<Recepcion> listaRecepcionados = recepcionRepository.findAllByTicketFaseID2AndUsuarioRol4();
        List<AtencionTicketDTO> listaRecepcionadosDTO = new ArrayList<>();
        for (Recepcion recepcion : listaRecepcionados) {
            AtencionTicketDTO recepcionadoDTO = new AtencionTicketDTO(recepcion, ticketService);
            listaRecepcionadosDTO.add(recepcionadoDTO);
        }
        return listaRecepcionadosDTO;
    }

    //metodo para retornar Mis tickets en proceso:
    public List<AtencionTicketDTO> getMyListaRecepcionados() {
        List<Recepcion> listaRecepcionados = recepcionRepository.findAllByTicketUsuarioId(usuarioService.getIDdeUsuarioLogeado());
        List<AtencionTicketDTO> listaRecepcionadosDTO = new ArrayList<>();
        for (Recepcion recepcion : listaRecepcionados) {
            AtencionTicketDTO recepcionadoDTO = new AtencionTicketDTO(recepcion, ticketService);
            listaRecepcionadosDTO.add(recepcionadoDTO);
        }
        return listaRecepcionadosDTO;
    }


    //metodo para retornar todos los tickets desestimados:
    public List<AtencionTicketDTO> getListaDesestimados() {
        List<Desestimacion> listaDesestimados = desestimacionRepository.findAllByTicketFaseID4();
        List<AtencionTicketDTO> listaDesestimadosDTO = new ArrayList<>();
        for (Desestimacion desestimacion : listaDesestimados) {
            AtencionTicketDTO desestimadoDTO = new AtencionTicketDTO(desestimacion, ticketService);
            listaDesestimadosDTO.add(desestimadoDTO);
        }
        return listaDesestimadosDTO;
    }
    //metodo para retornar Mis tickets atendidos:
    public List<AtencionTicketDTO> getMyListaDesestimados() {
        List<Desestimacion> listaDesestimaciones = desestimacionRepository.findAllByTicketUsuarioId(usuarioService.getIDdeUsuarioLogeado());
        List<AtencionTicketDTO> listaDesestimacionesDTO = new ArrayList<>();
        for (Desestimacion desestimacion : listaDesestimaciones) {
            AtencionTicketDTO desestimacionDTO = new AtencionTicketDTO(desestimacion, ticketService);
            listaDesestimacionesDTO.add(desestimacionDTO);
        }
        return listaDesestimacionesDTO;
    }
    //metodo para retornar Mis tickets atendidos:
    public List<AtencionTicketDTO> getMyListaAtendidos() {
        List<Servicio> listaServicios = servicioRepository.findAllByTicketUsuarioId(usuarioService.getIDdeUsuarioLogeado());
        List<AtencionTicketDTO> listaServiciosDTO = new ArrayList<>();
        for (Servicio servicio : listaServicios) {
            AtencionTicketDTO servicioDTO = new AtencionTicketDTO(servicio, ticketService);
            listaServiciosDTO.add(servicioDTO);
        }
        return listaServiciosDTO;
    }

    //metodo para retornar historial de atencion:
    public List<AtencionTicketDTO> getListaHistorialAtencion() {
        List<Servicio> listaAtendidos = servicioRepository.findAllByTicketFaseID3();
        List<AtencionTicketDTO> listaServiciosDTO = new ArrayList<>();

        for (Servicio servicio : listaAtendidos) {
            Recepcion recepcion = recepcionRepository.findByTicketId(servicio.getTicket().getId());
            AtencionTicketDTO servicioDTO = new AtencionTicketDTO(servicio, recepcion,ticketService);
            listaServiciosDTO.add(servicioDTO);
        }
        return listaServiciosDTO;
    }

    //cambiar fase de ticket para recepcion o atención
    public void updateFaseTicket(Long idTicket, Long idFaseTicket) {
        // Buscar el ticket existente por ID
        Ticket ticketExistente = ticketService.getObtenerTicketPorID(idTicket);
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
        System.out.println("guardando desestimacion:" + desestimacion.getId());
    }

    public void deleteRecepcion(Recepcion recepcion){
        recepcionRepository.deleteByTicketId(recepcion.getTicket().getId());
        System.out.println("Service: " + recepcion.getTicket().getId());
    }
    public Recepcion findRecepcionByTicketID(Long TicketID){
        return recepcionRepository.findByTicketId(TicketID);
    }

}
