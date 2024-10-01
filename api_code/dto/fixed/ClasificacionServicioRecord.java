package com.ipor.ticketsystem.api_code.dto.fixed;

import com.ipor.ticketsystem.model.fixed.ClasificacionServicio;

public record ClasificacionServicioRecord(
        Long id,
        String nombre
) {
    public ClasificacionServicioRecord(ClasificacionServicio clasificacionServicio) {
        this(clasificacionServicio.getId(), clasificacionServicio.getNombre());
    }
}
