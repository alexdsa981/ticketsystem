package com.ipor.ticketsystem.dashboard.admin.otros;

import com.ipor.ticketsystem.ticket.repository.AtencionRepository;
import com.ipor.ticketsystem.ticket.repository.DesestimacionRepository;
import com.ipor.ticketsystem.ticket.repository.RecepcionRepository;
import com.ipor.ticketsystem.ticket.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardClasificadoresService {

    @Autowired
    private RecepcionRepository recepcionRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private AtencionRepository atencionRepository;
    @Autowired
    DesestimacionRepository desestimacionRepository;

    //PARA OBTENER EL NUMERO TOTAL DE REGISTROS EN LA BD
    public long obtenerNTotalTickets(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return ticketRepository.countTicketsByFechaBetween(fechaInicio, fechaFin);
        }
        return ticketRepository.count();
    }

    public long obtenerNTotalRecepcionados(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return recepcionRepository.countRecepcionadosByFechaBetween(fechaInicio, fechaFin);
        }
        return recepcionRepository.count();
    }

    public long obtenerNTotalAtendidos(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return atencionRepository.countAtendidosByFechaBetween(fechaInicio, fechaFin);
        }
        return atencionRepository.count();
    }

    public long obtenerNTotalDesestimados(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return desestimacionRepository.countDesestimadosByFechaBetween(fechaInicio, fechaFin);
        }
        return desestimacionRepository.count();
    }

}
