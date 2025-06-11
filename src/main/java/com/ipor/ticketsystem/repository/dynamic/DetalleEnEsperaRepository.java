package com.ipor.ticketsystem.repository.dynamic;

import com.ipor.ticketsystem.model.dynamic.Desestimacion;
import com.ipor.ticketsystem.model.dynamic.DetalleEnEspera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DetalleEnEsperaRepository extends JpaRepository<DetalleEnEspera, Long> {

    @Query(value = "SELECT d.* FROM detalle_en_espera d INNER JOIN ticket t ON d.id_ticket = t.id WHERE t.id_usuario = :idUsuario AND t.id_fase_ticket = 5 ORDER BY d.fecha_inicio DESC, d.hora_inicio DESC", nativeQuery = true)
    List<DetalleEnEspera> findAllByTicketUsuarioId(@Param("idUsuario") Long idUsuario);

    @Query(value = "SELECT d.* FROM detalle_en_espera d INNER JOIN ticket t ON d.id_ticket = t.id WHERE t.id_fase_ticket = 5 ORDER BY d.fecha_inicio DESC, d.hora_inicio DESC", nativeQuery = true)
    List<DetalleEnEspera> findAllByTicketFaseID5();

    long count();

    @Query("SELECT COUNT(d) FROM DetalleEnEspera d WHERE d.fechaInicio BETWEEN :inicio AND :fin")
    long countEnEsperaByFechaBetween(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);
}