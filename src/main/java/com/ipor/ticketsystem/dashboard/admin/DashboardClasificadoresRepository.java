package com.ipor.ticketsystem.dashboard.admin;

import com.ipor.ticketsystem.dashboard.RecordFactorXConteo;

import java.time.LocalDate;
import java.util.List;

public interface DashboardClasificadoresRepository {

        List<RecordFactorXConteo> obtenerConteoDinamico(
                String agruparPor,
                LocalDate fechaInicio, LocalDate fechaFin,
                Long idSede, Long idArea, Long idCategoria, Long idSubcategoria,
                Long idTipoIncidencia, Long idTipoUrgencia,  Long idUsuario
        );


        List<Long> obtenerIdsTicketsFiltrados(
                LocalDate fechaInicio, LocalDate fechaFin,
                Long idSede, Long idArea, Long idCategoria, Long idSubcategoria,
                Long idTipoIncidencia, Long idTipoUrgencia, Long idUsuario
        );
}
