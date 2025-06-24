package com.ipor.ticketsystem.ticket.repository;

import com.ipor.ticketsystem.ticket.Desestimacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DesestimacionRepository extends JpaRepository<Desestimacion, Long> {

    @Query(value = "SELECT d.* FROM desestimacion d INNER JOIN ticket t ON d.id_ticket = t.id WHERE t.id_usuario = :idUsuario AND t.id_fase_ticket = 4 ORDER BY d.fecha DESC, d.hora DESC", nativeQuery = true)
    List<Desestimacion> findAllByTicketUsuarioId(@Param("idUsuario") Long idUsuario);

    @Query(value = "SELECT d.* FROM desestimacion d INNER JOIN ticket t ON d.id_ticket = t.id WHERE t.id_fase_ticket = 4 ORDER BY d.fecha DESC, d.hora DESC", nativeQuery = true)
    List<Desestimacion> findAllByTicketFaseID4();

    //numero total
    long count();

    @Query("SELECT COUNT(d) FROM Desestimacion d WHERE d.fecha BETWEEN :inicio AND :fin")
    long countDesestimadosByFechaBetween(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);
}
