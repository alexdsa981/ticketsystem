package com.ipor.ticketsystem.service;


import com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo;
import com.ipor.ticketsystem.repository.dynamic.DesestimacionRepository;
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
    DesestimacionRepository desestimacionRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
            return servicioRepository.countAtendidosByFechaBetween(fechaInicio, fechaFin);
        }
        return servicioRepository.count();
    }

    public long obtenerNTotalDesestimados(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return desestimacionRepository.countDesestimadosByFechaBetween(fechaInicio, fechaFin);
        }
        return desestimacionRepository.count();
    }



    //INFO PARA TABLA Y GRAFICO CIRCULAR
    //Conteo Clasificaciones Incidencia (DASHBOARD ADMIN)
    public List<RecordFactorXConteo> obtenerConteoDeTicketsPorClasificacionIncidencia(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return ticketRepository.findTicketCountByClasificacionIncidenciaWithDates(fechaInicio, fechaFin);
        }
        return ticketRepository.findTicketCountByClasificacionIncidencia();
    }

    //Conteo Clasificaciones Urgencia (DASHBOARD ADMIN)
    public List<RecordFactorXConteo> obtenerConteoDeTicketsPorClasificacionUrgencia(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return ticketRepository.findTicketCountByClasificacionUrgenciaWithDates(fechaInicio, fechaFin);
        }
        return ticketRepository.findTicketCountByClasificacionUrgencia();
    }

    //Conteo Áreas (DASHBOARD ADMIN)
    public List<RecordFactorXConteo> obtenerConteoDeTicketsPorArea(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return ticketRepository.findTicketCountByAreaWithDates(fechaInicio, fechaFin);
        }
        return ticketRepository.findTicketCountByArea();
    }





    //INFO PARA GRAFICOS EN PESTAÑA SOPORTE
    //Conteo de tickets por fase con filtro de fechas (ESTADO ACTUAL PARA DASHBOARD DE SOPORTE)
    public List<RecordFactorXConteo> obtenerConteoDeTicketsPorFase(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return ticketRepository.findTicketCountByFaseWithDates(fechaInicio, fechaFin);
        }
        return ticketRepository.findTicketCountByFase();
    }





    //INFO DE TIEMPO

    public double obtenerPromedioSegundosR_S(LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            if (fechaInicio != null && fechaFin != null) {
                Double promedio = ticketRepository.obtenerPromedioSegundosRSConFecha(fechaInicio, fechaFin);
                return promedio != null ? promedio : 0.0;
            }
            return ticketRepository.obtenerPromedioSegundosRS(); // método general ya existente
        } catch (Exception e) {
            return 0.0;
        }
    }


    public double obtenerPromedioSegundosT_R(LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            if (fechaInicio != null && fechaFin != null) {
                Double promedio = ticketRepository.obtenerPromedioSegundosTRConFecha(fechaInicio, fechaFin);
                return promedio != null ? promedio : 0.0;
            }
            return ticketRepository.obtenerPromedioSegundosTR();
        } catch (Exception e) {
            return 0.0;
        }
    }

    public double obtenerPromedioSegundosT_S(LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            if (fechaInicio != null && fechaFin != null) {
                Double promedio = ticketRepository.obtenerPromedioSegundosTSConFecha(fechaInicio, fechaFin);
                return promedio != null ? promedio : 0.0;
            }
            return ticketRepository.obtenerPromedioSegundosTS();
        } catch (Exception e) {
            return 0.0;
        }
    }



}


