package com.ipor.ticketsystem.service;


import com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo;
import com.ipor.ticketsystem.model.dynamic.Ticket;
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


    public List<Ticket> Obtener5TicketsMasRecientes() {
        return ticketRepository.findTop5ByFaseTicket_IdNotInOrderByFechaDescHoraDesc(List.of(3L, 4L));
    }






    //INFO DE TIEMPO

    public double obtenerPromedioSegundosTicket_Recepcion(LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            if (fechaInicio != null && fechaFin != null) {
                Double segundosTR = ticketRepository.obtenerSegundosTicketRecepcionConFecha(fechaInicio, fechaFin);
                Double segundosEspera1 = ticketRepository.obtenerSegundosEsperaClasificacion1ConFecha(fechaInicio, fechaFin);
                Long totalTicketsAtendidos = ticketRepository.contarTicketsAtendidosEnRango(fechaInicio, fechaFin);

                double STR = segundosTR != null ? segundosTR : 0.0;
                double SE = segundosEspera1 != null ? segundosEspera1 : 0.0;
                long TTA = totalTicketsAtendidos != null ? totalTicketsAtendidos : 0;

                return TTA > 0 ? (STR - SE) / TTA : 0.0;
            }

            Double segundosTR = ticketRepository.obtenerSegundosTicketRecepcion();
            Double segundosEspera1 = ticketRepository.obtenerSegundosEsperaClasificacion1();
            Long totalTicketsAtendidos = ticketRepository.contarTicketsAtendidos();

            double STR = segundosTR != null ? segundosTR : 0.0;
            double SE = segundosEspera1 != null ? segundosEspera1 : 0.0;
            long TTA = totalTicketsAtendidos != null ? totalTicketsAtendidos : 0;

            return TTA > 0 ? (STR - SE) / TTA : 0.0;

        } catch (Exception e) {
            return 0.0;
        }
    }




    public double obtenerPromedioSegundosRecepcion_Atencion(LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            if (fechaInicio != null && fechaFin != null) {
                Double segundosRA = ticketRepository.obtenerSegundosRecepcionAtencionConFecha(fechaInicio, fechaFin);
                Double segundosEsperaNo1 = ticketRepository.obtenerSegundosEsperaClasificacionNo1ConFecha(fechaInicio, fechaFin);
                Long totalTicketsAtendidos = ticketRepository.contarTicketsAtendidosEnRango(fechaInicio, fechaFin);

                double SRA = segundosRA != null ? segundosRA : 0.0;
                double SE = segundosEsperaNo1 != null ? segundosEsperaNo1 : 0.0;
                long TTA = totalTicketsAtendidos != null ? totalTicketsAtendidos : 0;

                return TTA > 0 ? (SRA - SE) / TTA : 0.0;
            }

            Double segundosRA = ticketRepository.obtenerSegundosRecepcionAtencion();
            Double segundosEsperaNo1 = ticketRepository.obtenerSegundosEsperaClasificacionNo1();
            Long totalTicketsAtendidos = ticketRepository.contarTicketsAtendidos();

            double SRA = segundosRA != null ? segundosRA : 0.0;
            double SE = segundosEsperaNo1 != null ? segundosEsperaNo1 : 0.0;
            long TTA = totalTicketsAtendidos != null ? totalTicketsAtendidos : 0;

            return TTA > 0 ? (SRA - SE) / TTA : 0.0;

        } catch (Exception e) {
            return 0.0;
        }
    }





    public double obtenerPromedioSegundosTicket_Atencion(LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            if (fechaInicio != null && fechaFin != null) {
                Double segundosTA = ticketRepository.obtenerSegundosTicketAtencionConFecha(fechaInicio, fechaFin);
                Double segundosEsperaNo1 = ticketRepository.obtenerSegundosEsperaClasificacionNo1ConFecha(fechaInicio, fechaFin);
                Double segundosEspera1 = ticketRepository.obtenerSegundosEsperaClasificacion1ConFecha(fechaInicio, fechaFin);
                Long totalTicketsAtendidos = ticketRepository.contarTicketsAtendidosEnRango(fechaInicio, fechaFin);

                double STA = segundosTA != null ? segundosTA : 0.0;
                double SEN1 = segundosEsperaNo1 != null ? segundosEsperaNo1 : 0.0;
                double SE1 = segundosEspera1 != null ? segundosEspera1 : 0.0;
                long TTA = totalTicketsAtendidos != null ? totalTicketsAtendidos : 0;

                return TTA > 0 ? (STA - (SEN1 + SE1)) / TTA : 0.0;
            }

            Double segundosTA = ticketRepository.obtenerSegundosTicketAtencion();
            Double segundosEsperaNo1 = ticketRepository.obtenerSegundosEsperaClasificacionNo1();
            Double segundosEspera1 = ticketRepository.obtenerSegundosEsperaClasificacion1();
            Long totalTicketsAtendidos = ticketRepository.contarTicketsAtendidos();

            double STA = segundosTA != null ? segundosTA : 0.0;
            double SEN1 = segundosEsperaNo1 != null ? segundosEsperaNo1 : 0.0;
            double SE1 = segundosEspera1 != null ? segundosEspera1 : 0.0;
            long TTA = totalTicketsAtendidos != null ? totalTicketsAtendidos : 0;

            return TTA > 0 ? (STA - (SEN1 + SE1)) / TTA : 0.0;

        } catch (Exception e) {
            return 0.0;
        }
    }



}


