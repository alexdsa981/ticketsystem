package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dto.DetalleTicketDTO;
import com.ipor.ticketsystem.model.dynamic.*;
import com.ipor.ticketsystem.repository.dynamic.DesestimacionRepository;
import com.ipor.ticketsystem.repository.dynamic.DetalleEnEsperaRepository;
import com.ipor.ticketsystem.repository.dynamic.RecepcionRepository;
import com.ipor.ticketsystem.repository.dynamic.AtencionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        for (DetalleEnEspera detalleEnEspera : listaDetalleEnEspera) {
            DetalleTicketDTO detalleEnEsperaDTO = new DetalleTicketDTO(detalleEnEspera.getTicket());
            listaDetallesEsperaDTO.add(detalleEnEsperaDTO);
        }
        return listaDetallesEsperaDTO;
    }
    //metodo para retornar Mis tickets en espera:
    public List<DetalleTicketDTO> getMyListaEspera() {
        List<DetalleEnEspera> listaDetalleEnEspera = detalleEnEsperaRepository.findAllByTicketUsuarioId(usuarioService.getIDdeUsuarioLogeado());
        List<DetalleTicketDTO> listaDetallesEsperaDTO = new ArrayList<>();
        for (DetalleEnEspera detalleEnEspera : listaDetalleEnEspera) {
            DetalleTicketDTO detalleEnEsperaDTO = new DetalleTicketDTO(detalleEnEspera.getTicket());
            listaDetallesEsperaDTO.add(detalleEnEsperaDTO);
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
