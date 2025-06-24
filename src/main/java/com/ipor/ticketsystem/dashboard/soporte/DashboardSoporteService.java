package com.ipor.ticketsystem.dashboard.soporte;

import com.ipor.ticketsystem.dashboard.RecordFactorXConteo;
import com.ipor.ticketsystem.ticket.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DashboardSoporteService {
    @Autowired
    DashboardSoporteTicketRepository dashboardSoporteTicketRepository;
    //INFO PARA GRAFICOS EN PESTAÑA SOPORTE
    //Conteo de tickets por fase con filtro de fechas (ESTADO ACTUAL PARA DASHBOARD DE SOPORTE)
    public List<RecordFactorXConteo> obtenerConteoDeTicketsPorFase(LocalDate fechaInicio, LocalDate fechaFin) {

        if (fechaInicio != null && fechaFin != null) {
            return dashboardSoporteTicketRepository.findTicketCountByFaseWithDates(fechaInicio, fechaFin);
        }
        return dashboardSoporteTicketRepository.findTicketCountByFase();
    }


    public List<Ticket> Obtener5TicketsMasRecientes() {
        return dashboardSoporteTicketRepository.findTop5ByFaseTicket_IdNotInOrderByFechaDescHoraDesc(List.of(3L, 4L));
    }

    public List<TicketInactivoProjection> obtenerTicketsInactivosMasAntiguos() {
        return dashboardSoporteTicketRepository.obtenerTicketsInactivosMasAntiguos();
    }

}
