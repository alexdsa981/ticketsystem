package com.ipor.ticketsystem.service;


import com.ipor.ticketsystem.repository.dynamic.RecepcionRepository;
import com.ipor.ticketsystem.repository.dynamic.ServicioRepository;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private RecepcionRepository recepcionRepository;
    @Autowired
    private ServicioRepository servicioRepository;

    public long obtenerNTotalTickets() {
        return ticketRepository.count();
    }
    public long obtenerNTotalRecepcionados() {
        return recepcionRepository.count();
    }
    public long obtenerNTotalAtendidos() {
        return servicioRepository.count();
    }
    public long obtenerNTotalDesestimados() {
        return ticketRepository.countByFaseTicketNombre("Desestimado");
    }

}