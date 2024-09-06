package com.ipor.ticketsystem.dto.fixed;

import com.ipor.ticketsystem.model.fixed.ClasificacionUrgencia;

public record ClasificacionUrgenciaRecord(
        Long id,
        String nombre
) {
    public ClasificacionUrgenciaRecord(ClasificacionUrgencia clasificacionUrgencia) {
        this(clasificacionUrgencia.getId(), clasificacionUrgencia.getNombre());
    }
}