package com.ipor.ticketsystem.service;


import com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo;
import com.ipor.ticketsystem.repository.dynamic.RecepcionRepository;
import com.ipor.ticketsystem.repository.dynamic.ServicioRepository;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DashboardService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private RecepcionRepository recepcionRepository;
    @Autowired
    private ServicioRepository servicioRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //para obtener el numero en las tablas
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

    public double obtenerPromedioSegundosR_S() {
        return ticketRepository.obtenerPromedioSegundosRS();
    }

    public double obtenerPromedioSegundosT_R() {
        return ticketRepository.obtenerPromedioSegundosTR();
    }

    public double obtenerPromedioSegundosT_S() {
        return ticketRepository.obtenerPromedioSegundosTS();
    }


}


