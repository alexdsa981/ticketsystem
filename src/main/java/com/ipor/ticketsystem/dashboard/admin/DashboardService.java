package com.ipor.ticketsystem.dashboard.admin;

import com.ipor.ticketsystem.dashboard.RecordFactorXConteo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    public DashboardService(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    public List<RecordFactorXConteo> obtenerConteoDinamico(String agruparPor,
                                                           LocalDate fechaInicio, LocalDate fechaFin,
                                                           Long idSede, Long idArea, Long idCategoria,
                                                           Long idSubcategoria, Long idTipoIncidencia, Long idTipoUrgencia,
                                                           Long idUsuario) {
        return dashboardRepository.obtenerConteoDinamico(
                agruparPor, fechaInicio, fechaFin,
                idSede, idArea, idCategoria, idSubcategoria,
                idTipoIncidencia, idTipoUrgencia, idUsuario
        );
    }

    public List<Long> obtenerIdsTicketsFiltrados(
            LocalDate fechaInicio, LocalDate fechaFin,
            Long idSede, Long idArea, Long idCategoria, Long idSubcategoria,
            Long idTipoIncidencia, Long idTipoUrgencia, Long idUsuario) {
        return dashboardRepository.obtenerIdsTicketsFiltrados(
                fechaInicio, fechaFin, idSede, idArea, idCategoria,
                idSubcategoria, idTipoIncidencia, idTipoUrgencia, idUsuario);
    }
}
