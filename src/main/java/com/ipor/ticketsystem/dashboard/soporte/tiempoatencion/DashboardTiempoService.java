package com.ipor.ticketsystem.dashboard.soporte.tiempoatencion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class DashboardTiempoService {
    @Autowired
    DashboardAdminTiempoRepository dashboardAdminTiempoRepository;

    public double obtenerPromedioSegundosTicket_Recepcion(LocalDate fechaInicio, LocalDate fechaFin) {
        try {

            Double segundosTR = dashboardAdminTiempoRepository.obtenerSegundosTicketRecepcionConFecha(fechaInicio, fechaFin);
            Double segundosEspera1 = dashboardAdminTiempoRepository.obtenerSegundosEsperaClasificacion1ConFecha(fechaInicio, fechaFin);
            Long totalTicketsAtendidos = dashboardAdminTiempoRepository.contarTicketsAtendidosEnRango(fechaInicio, fechaFin);

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
            Double segundosRA = dashboardAdminTiempoRepository.obtenerSegundosRecepcionAtencionConFecha(fechaInicio, fechaFin);
            Double segundosEsperaNo1 = dashboardAdminTiempoRepository.obtenerSegundosEsperaClasificacionNo1ConFecha(fechaInicio, fechaFin);
            Long totalTicketsAtendidos = dashboardAdminTiempoRepository.contarTicketsAtendidosEnRango(fechaInicio, fechaFin);

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
            Double segundosTA = dashboardAdminTiempoRepository.obtenerSegundosTicketAtencionConFecha(fechaInicio, fechaFin);
            Double segundosEsperaNo1 = dashboardAdminTiempoRepository.obtenerSegundosEsperaClasificacionNo1ConFecha(fechaInicio, fechaFin);
            Double segundosEspera1 = dashboardAdminTiempoRepository.obtenerSegundosEsperaClasificacion1ConFecha(fechaInicio, fechaFin);
            Long totalTicketsAtendidos = dashboardAdminTiempoRepository.contarTicketsAtendidosEnRango(fechaInicio, fechaFin);

            double STA = segundosTA != null ? segundosTA : 0.0;
            double SEN1 = segundosEsperaNo1 != null ? segundosEsperaNo1 : 0.0;
            double SE1 = segundosEspera1 != null ? segundosEspera1 : 0.0;
            long TTA = totalTicketsAtendidos != null ? totalTicketsAtendidos : 0;

            return TTA > 0 ? (STA - (SEN1 + SE1)) / TTA : 0.0;

        } catch (Exception e) {
            return 0.0;
        }
    }


    public Map<String, Object> obtenerTiempoEfectivoYCantidadTicketsPorUsuario(Long idUsuario, LocalDate fechaInicio, LocalDate fechaFin) {
        Double tiempoAtencionSegundos = dashboardAdminTiempoRepository.obtenerTiempoAtencionEnSegundosPorUsuario(idUsuario, fechaInicio, fechaFin);
        Double tiempoEsperaSegundos = dashboardAdminTiempoRepository.obtenerTiempoEsperaEnSegundosPorUsuario(idUsuario, fechaInicio, fechaFin);
        Long cantidadTicketsAtendidos = dashboardAdminTiempoRepository.contarTicketsAtendidosPorUsuarioEnRango(idUsuario, fechaInicio, fechaFin);

        if (tiempoAtencionSegundos == null) tiempoAtencionSegundos = 0.0;
        if (tiempoEsperaSegundos == null) tiempoEsperaSegundos = 0.0;
        if (cantidadTicketsAtendidos == null || cantidadTicketsAtendidos == 0) {
            return Map.of(
                    "promedioMinutos", 0.0,
                    "cantidadTickets", 0
            );
        }

        double tiempoEfectivoTotal = tiempoAtencionSegundos - tiempoEsperaSegundos;
        if (tiempoEfectivoTotal < 0) tiempoEfectivoTotal = 0.0;

        double promedioMinutos = (tiempoEfectivoTotal / cantidadTicketsAtendidos) / 60.0;

        return Map.of(
                "promedioMinutos", promedioMinutos,
                "cantidadTickets", cantidadTicketsAtendidos
        );
    }
}
