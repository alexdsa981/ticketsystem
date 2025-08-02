package com.ipor.ticketsystem.dashboard.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class IndicadorResolucionDTO {
    private long total;
    private long resueltos;
    private long errorUsuario;

    public IndicadorResolucionDTO(long total, long resueltos, long errorUsuario) {
        this.total = total;
        this.resueltos = resueltos;
        this.errorUsuario = errorUsuario;
    }

    public double getPorcentajeResueltos() {
        return total == 0 ? 0 : (resueltos * 100.0 / total);
    }

    public double getPorcentajeNoResueltos() {
        return 100.0 - getPorcentajeResueltos();
    }

    public double getPorcentajeErrorUsuario() {
        return resueltos == 0 ? 0 : (errorUsuario * 100.0 / resueltos);
    }
}
