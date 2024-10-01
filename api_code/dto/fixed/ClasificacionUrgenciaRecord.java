package com.ipor.ticketsystem.api_code.dto.fixed;

import com.ipor.ticketsystem.model.fixed.ClasificacionUrgencia;

public record ClasificacionUrgenciaRecord(
        Long id,
        String nombre
) {
    public ClasificacionUrgenciaRecord(ClasificacionUrgencia clasificacionUrgencia) {
        this(clasificacionUrgencia.getId(), clasificacionUrgencia.getNombre());
    }
}