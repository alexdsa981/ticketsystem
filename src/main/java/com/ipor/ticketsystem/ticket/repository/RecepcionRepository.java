package com.ipor.ticketsystem.ticket.repository;

import com.ipor.ticketsystem.ticket.Recepcion;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RecepcionRepository extends JpaRepository<Recepcion, Long> {
    @Query(value = "SELECT r.* FROM recepcion r INNER JOIN ticket t ON r.id_ticket = t.id WHERE t.id_usuario = :idUsuario and t.id_fase_ticket= 2 ORDER BY r.fecha DESC, r.hora DESC", nativeQuery = true)
    List<Recepcion> findAllByTicketUsuarioId(@Param("idUsuario") Long idUsuario);

    @Query(value = "SELECT r.* FROM recepcion r INNER JOIN ticket t ON r.id_ticket = t.id WHERE t.id_fase_ticket = 2 ORDER BY r.fecha DESC, r.hora DESC", nativeQuery = true)
    List<Recepcion> findAllByTicketFaseID2();



    @Query(value = "SELECT r.* FROM recepcion r WHERE r.id_ticket = :idTicket", nativeQuery = true)
    Recepcion findByTicketId(@Param("idTicket") Long idTicket);

    @Transactional
    @Modifying
    @Query("DELETE FROM Recepcion r WHERE r.ticket.id = :idTicket")
    void deleteByTicketId(@Param("idTicket") Long idTicket);

    //numero total
    long count();

    @Query("SELECT COUNT(r) FROM Recepcion r WHERE r.fecha BETWEEN :inicio AND :fin")
    long countRecepcionadosByFechaBetween(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);
}
