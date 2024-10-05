package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dto.AtencionTicketDTO;
import com.ipor.ticketsystem.model.dynamic.Recepcion;
import com.ipor.ticketsystem.model.dynamic.Servicio;
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
    //metodo para retornar todos los tickets en proceso:
    public List<AtencionTicketDTO> getListaRecepcionados(){
        List<Recepcion> listaRecepcionados = recepcionRepository.findAll();
        List<AtencionTicketDTO> listaRecepcionadosDTO = new ArrayList<>();
        for (Recepcion recepcion : listaRecepcionados){
            AtencionTicketDTO recepcionadoDTO = new AtencionTicketDTO(recepcion);
            listaRecepcionadosDTO.add(recepcionadoDTO);
        }
        return listaRecepcionadosDTO;
    }
    //metodo para retornar Mis tickets en proceso:
    public List<AtencionTicketDTO> getMyListaRecepcionados(){
        List<Recepcion> listaRecepcionados = recepcionRepository.findAll();
        List<AtencionTicketDTO> listaRecepcionadosDTO = new ArrayList<>();
        for (Recepcion recepcion : listaRecepcionados){
            AtencionTicketDTO recepcionadoDTO = new AtencionTicketDTO(recepcion);
            listaRecepcionadosDTO.add(recepcionadoDTO);
        }
        return listaRecepcionadosDTO;
    }
    //metodo para retornar todos los tickets atendidos:
    public List<AtencionTicketDTO> getListaAtendidos(){
        List<Servicio> listaAtendidos = servicioRepository.findAll();
        List<AtencionTicketDTO> listaAtendidosDTO = new ArrayList<>();
        for (Servicio servicio : listaAtendidos){
            AtencionTicketDTO recepcionadoDTO = new AtencionTicketDTO(servicio);
            listaAtendidosDTO.add(recepcionadoDTO);
        }
        return listaAtendidosDTO;
    }




}
