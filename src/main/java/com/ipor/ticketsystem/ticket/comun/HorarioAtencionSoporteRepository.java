package com.ipor.ticketsystem.ticket.comun;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HorarioAtencionSoporteRepository extends JpaRepository<HorarioAtencionSoporte, Long> {
    HorarioAtencionSoporte findTopByOrderByIdDesc();
}
