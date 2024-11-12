package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dto.AtencionTicketDTO;
import com.ipor.ticketsystem.model.dynamic.Desestimacion;
import com.ipor.ticketsystem.model.dynamic.Recepcion;
import com.ipor.ticketsystem.model.dynamic.Servicio;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.model.fixed.ClasificacionDesestimacion;
import com.ipor.ticketsystem.model.fixed.ClasificacionServicio;
import com.ipor.ticketsystem.model.fixed.ClasificacionUrgencia;
import com.ipor.ticketsystem.repository.dynamic.DesestimacionRepository;
import com.ipor.ticketsystem.repository.dynamic.RecepcionRepository;
import com.ipor.ticketsystem.repository.dynamic.ServicioRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionDesestimacionRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionServicioRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionUrgenciaRepository;
import jakarta.transaction.Transactional;
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
    ClasificacionUrgenciaRepository clasificacionUrgenciaRepository;
    @Autowired
    ClasificacionServicioRepository clasificacionServicioRepository;
    @Autowired
    ClasificacionDesestimacionRepository clasificacionDesestimacionRepository;
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

    //metodo para retornar Mis tickets en proceso:
    public List<AtencionTicketDTO> getMyListaRecepcionados() {
        List<Recepcion> listaRecepcionados = recepcionRepository.findAllByTicketUsuarioId(usuarioService.RetornarIDdeUsuarioLogeado());
        List<AtencionTicketDTO> listaRecepcionadosDTO = new ArrayList<>();
        for (Recepcion recepcion : listaRecepcionados) {
            AtencionTicketDTO recepcionadoDTO = new AtencionTicketDTO(recepcion, ticketService);
            listaRecepcionadosDTO.add(recepcionadoDTO);
        }
        return listaRecepcionadosDTO;
    }

    //metodo para retornar todos los tickets atendidos:
    public List<AtencionTicketDTO> getListaAtendidos() {
        List<Servicio> listaAtendidos = servicioRepository.findAllByTicketFaseID3();
        List<AtencionTicketDTO> listaAtendidosDTO = new ArrayList<>();
        for (Servicio servicio : listaAtendidos) {
            AtencionTicketDTO atendidoDTO = new AtencionTicketDTO(servicio, ticketService);
            listaAtendidosDTO.add(atendidoDTO);
        }
        return listaAtendidosDTO;
    }

    //metodo para retornar Mis tickets atendidos:
    public List<AtencionTicketDTO> getMyListaAtendidos() {
        List<Servicio> listaServicios = servicioRepository.findAllByTicketUsuarioId(usuarioService.RetornarIDdeUsuarioLogeado());
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


    //obtener clasificacion urgencia por id
    public ClasificacionUrgencia obtenerClasificacionUrgenciaPorId(Long id){
        return clasificacionUrgenciaRepository.findById(id).get();
    }
    //obtener clasificaciones urgencia
    public List<ClasificacionUrgencia> obtenerListaClasificacionUrgencia(){
        return clasificacionUrgenciaRepository.findByIsActiveTrue();
    }

    //obtener clasificacion servicio por id
    public ClasificacionServicio obtenerClasificacionServicioPorId(Long id){
        return clasificacionServicioRepository.findById(id).get();
    }
    //obtener clasificaciones servicio
    public List<ClasificacionServicio> obtenerListaClasificacionServicio(){
        return clasificacionServicioRepository.findByIsActiveTrue();
    }

    //obtener clasificacion desestimacion por id
    public ClasificacionDesestimacion obtenerClasificacionDesestimacionPorId(Long id){
        return clasificacionDesestimacionRepository.findById(id).get();
    }
    //obtener clasificaciones desestimacion
    public List<ClasificacionDesestimacion> obtenerListaClasificacionDesestimacion(){
        return clasificacionDesestimacionRepository.findByIsActiveTrue();
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
