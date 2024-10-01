package com.ipor.ticketsystem.api_code.dto.fixed;

import com.ipor.ticketsystem.model.fixed.ClasificacionIncidencia;

public record ClasificacionIncidenciaRecord(
        Long id,
        String nombre
) {
    public ClasificacionIncidenciaRecord(ClasificacionIncidencia clasificacionIncidencia) {
        this(clasificacionIncidencia.getId(), clasificacionIncidencia.getNombre());
    }
}
