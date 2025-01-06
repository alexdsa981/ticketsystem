package com.ipor.ticketsystem.service;


import com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo;
import com.ipor.ticketsystem.repository.dynamic.RecepcionRepository;
import com.ipor.ticketsystem.repository.dynamic.ServicioRepository;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DashboardService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private RecepcionRepository recepcionRepository;
    @Autowired
    private ServicioRepository servicioRepository;

    //para obtener el numero en las tablas
    public long obtenerNTotalTickets() {
        return ticketRepository.count();
    }
    public long obtenerNTotalRecepcionados() {
        return recepcionRepository.count();
    }
    public long obtenerNTotalAtendidos() {return servicioRepository.count();}
    public long obtenerNTotalDesestimados() {
        return ticketRepository.countByFaseTicketNombre("Desestimado");
    }
    public long obtenerNTotalRedireccionados() {
        return ticketRepository.countByFaseTicketNombre("Espera en Dirección");
    }



    // ✅ Conteo de tickets por fase con filtro de fechas
    public List<RecordFactorXConteo> obtenerConteoDeTicketsPorFase(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return ticketRepository.findTicketCountByFaseWithDates(fechaInicio, fechaFin);
        }
        return ticketRepository.findTicketCountByFase();
    }

    // ✅ Conteo de tickets por clasificación de incidencia con filtro de fechas
    public List<RecordFactorXConteo> obtenerConteoDeTicketsPorClasificacionIncidencia(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return ticketRepository.findTicketCountByClasificacionIncidenciaWithDates(fechaInicio, fechaFin);
        }
        return ticketRepository.findTicketCountByClasificacionIncidencia();
    }

    // ✅ Conteo de tickets por clasificación de urgencia con filtro de fechas
    public List<RecordFactorXConteo> obtenerConteoDeTicketsPorClasificacionUrgencia(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return ticketRepository.findTicketCountByClasificacionUrgenciaWithDates(fechaInicio, fechaFin);
        }
        return ticketRepository.findTicketCountByClasificacionUrgencia();
    }

    // ✅ Conteo de componentes adjuntos aprobados por tipo con filtro de fechas
    public List<RecordFactorXConteo> obtenerConteoDeComponentesAdjuntosAprobados(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return ticketRepository.findGroupedByTipoComponenteAprobadoWithDates(fechaInicio, fechaFin);
        }
        return ticketRepository.findGroupedByTipoComponenteAprobado();
    }
}


