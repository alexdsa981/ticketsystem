package com.ipor.ticketsystem.repository.fixed;

import com.ipor.ticketsystem.model.fixed.HorarioAtencionSoporte;
import com.ipor.ticketsystem.model.fixed.Sede;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HorarioAtencionSoporteRepository extends JpaRepository<HorarioAtencionSoporte, Long> {
    HorarioAtencionSoporte findTopByOrderByIdDesc();
}
