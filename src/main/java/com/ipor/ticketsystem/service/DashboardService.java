package com.ipor.ticketsystem.service;


import com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo;
import com.ipor.ticketsystem.repository.dynamic.DesestimacionRepository;
import com.ipor.ticketsystem.repository.dynamic.RecepcionRepository;
import com.ipor.ticketsystem.repository.dynamic.AtencionRepository;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
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
    private AtencionRepository atencionRepository;
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


    //INFO PARA TABLA Y GRAFICO CIRCULAR
    //Conteo Clasificaciones Incidencia (DASHBOARD ADMIN)
    public List<RecordFactorXConteo> obtenerConteoDeTicketsPorCategoriaIncidencia(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return ticketRepository.findTicketCountByCATIncidenciaWithDates(fechaInicio, fechaFin);
        }
        return ticketRepository.findTicketCountByCATIncidencia();
    }

    public List<RecordFactorXConteo> obtenerConteoDeTicketsPorSubcategoriaIncidencia(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return ticketRepository.findTicketCountBySubCATIncidenciaWithDates(fechaInicio, fechaFin);
        }
        return ticketRepository.findTicketCountBySubCATIncidencia();
    }

    public List<RecordFactorXConteo> obtenerConteoDeTicketsPorTipoIncidencia(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return ticketRepository.findTicketCountByTipoIncidenciaWithDates(fechaInicio, fechaFin);
        }
        return ticketRepository.findTicketCountByTipoIncidencia();
    }

    //Conteo Clasificaciones Urgencia (DASHBOARD ADMIN)
    public List<RecordFactorXConteo> obtenerConteoDeTicketsPorClasificacionUrgencia(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return ticketRepository.findTicketCountByClasificacionUrgenciaWithDates(fechaInicio, fechaFin);
        }
        return ticketRepository.findTicketCountByClasificacionUrgencia();
    }

    //Conteo Sede (DASHBOARD ADMIN)
    public List<RecordFactorXConteo> obtenerConteoDeTicketsPorSede(LocalDate fechaInicio, LocalDate fechaFin) {

        if (fechaInicio != null && fechaFin != null) {
            return ticketRepository.findTicketCountBySedeWithDates(fechaInicio, fechaFin);
        }
        return ticketRepository.findTicketCountBySede();
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


