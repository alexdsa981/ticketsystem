package com.ipor.ticketsystem.ticket.service;

import com.ipor.ticketsystem.ticket.dto.DetalleTicketDTO;
import com.ipor.ticketsystem.usuario.UsuarioService;
import com.ipor.ticketsystem.ticket.repository.DesestimacionRepository;
import com.ipor.ticketsystem.ticket.repository.DetalleEnEsperaRepository;
import com.ipor.ticketsystem.ticket.repository.RecepcionRepository;
import com.ipor.ticketsystem.ticket.repository.AtencionRepository;
import com.ipor.ticketsystem.ticket.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AtencionService {
    @Autowired
    RecepcionRepository recepcionRepository;
    @Autowired
    AtencionRepository atencionRepository;
    @Autowired
    DesestimacionRepository desestimacionRepository;
    @Autowired
    DetalleEnEsperaRepository detalleEnEsperaRepository;
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


    //metodo para retornar todos los tickets en espera:
    public List<DetalleTicketDTO> getListaEspera() {
        List<DetalleEnEspera> listaDetalleEnEspera = detalleEnEsperaRepository.findAllByTicketFaseID5();
        List<DetalleTicketDTO> listaDetallesEsperaDTO = new ArrayList<>();
        Set<Long> idsAgregados = new HashSet<>();

        for (DetalleEnEspera detalleEnEspera : listaDetalleEnEspera) {
            Long idTicket = detalleEnEspera.getTicket().getId();
            if (!idsAgregados.contains(idTicket)) {
                DetalleTicketDTO detalleEnEsperaDTO = new DetalleTicketDTO(detalleEnEspera.getTicket());
                listaDetallesEsperaDTO.add(detalleEnEsperaDTO);
                idsAgregados.add(idTicket);
            }
        }
        return listaDetallesEsperaDTO;
    }

    //metodo para retornar Mis tickets en espera:
    public List<DetalleTicketDTO> getMyListaEspera() {
        Long idUsuarioLogueado = usuarioService.getIDdeUsuarioLogeado();
        List<DetalleEnEspera> listaDetalleEnEspera = detalleEnEsperaRepository.findAllByTicketUsuarioId(idUsuarioLogueado);
        List<DetalleTicketDTO> listaDetallesEsperaDTO = new ArrayList<>();
        Set<Long> idsAgregados = new HashSet<>();

        for (DetalleEnEspera detalleEnEspera : listaDetalleEnEspera) {
            Long idTicket = detalleEnEspera.getTicket().getId();
            if (!idsAgregados.contains(idTicket)) {
                DetalleTicketDTO detalleEnEsperaDTO = new DetalleTicketDTO(detalleEnEspera.getTicket());
                listaDetallesEsperaDTO.add(detalleEnEsperaDTO);
                idsAgregados.add(idTicket);
            }
        }
        return listaDetallesEsperaDTO;
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
        List<Atencion> listaAtencions = atencionRepository.findAllByTicketUsuarioId(usuarioService.getIDdeUsuarioLogeado());
        List<DetalleTicketDTO> listaAtencionsDTO = new ArrayList<>();
        for (Atencion atencion : listaAtencions) {
            DetalleTicketDTO atencionDTO = new DetalleTicketDTO(atencion.getTicket());
            listaAtencionsDTO.add(atencionDTO);
        }
        return listaAtencionsDTO;
    }

    //metodo para retornar historial de atencion:
    public List<DetalleTicketDTO> getListaHistorialAtencion() {
        List<Atencion> listaAtendidos = atencionRepository.findAllByTicketFaseID3();
        List<DetalleTicketDTO> listaAtencionsDTO = new ArrayList<>();

        for (Atencion atencion : listaAtendidos) {
            DetalleTicketDTO atencionDTO = new DetalleTicketDTO(atencion.getTicket());
            listaAtencionsDTO.add(atencionDTO);
        }
        return listaAtencionsDTO;
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
    //guardar atencion en base de datos
    public void saveAtencion(Atencion atencion){
        atencionRepository.save(atencion);
    }
    //guardar atencion en base de datos
    public void saveDesestimacion(Desestimacion desestimacion){
        desestimacionRepository.save(desestimacion);
    }
    public void saveEspera(DetalleEnEspera detalleEnEspera){
        detalleEnEsperaRepository.save(detalleEnEspera);
    }

    public void deleteRecepcion(Recepcion recepcion){
        recepcionRepository.deleteByTicketId(recepcion.getTicket().getId());
    }
    public Recepcion findRecepcionByTicketID(Long TicketID){
        return recepcionRepository.findByTicketId(TicketID);
    }

}
