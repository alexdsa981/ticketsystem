package com.ipor.ticketsystem.ticket.repository;

import com.ipor.ticketsystem.dashboard.RecordFactorXConteo;
import com.ipor.ticketsystem.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    

    // Consulta para obtener los 300 tickets recibidos más recientes
    @Query(value = "SELECT TOP 300 t.* FROM ticket t WHERE t.id_usuario = :idUsuario AND t.id_fase_ticket = 1 ORDER BY t.fecha DESC, t.hora DESC", nativeQuery = true)
    List<Ticket> findAllByTicketUsuarioId(@Param("idUsuario") Long idUsuario);


    @Query(value = "SELECT TOP 300 t.* FROM ticket t WHERE t.id_fase_ticket= 1 ORDER BY t.fecha DESC, t.hora DESC", nativeQuery = true)
    List<Ticket> findAllSinRecepcionar();

    //obtener tickets propios
    List<Ticket> findByUsuarioIdAndFaseTicketId(Long usuarioId, Long faseTicketId);
    //obtener ticket por codigo ticket ej: TK-0003
    Optional<Ticket> findByCodigoTicket(String codigo);

    @Query("SELECT MAX(CAST(SUBSTRING(t.codigoTicket, 4) AS int)) FROM Ticket t")
    Integer obtenerUltimoNumeroTicket();

    long count();


    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.fecha BETWEEN :inicio AND :fin")
    long countTicketsByFechaBetween(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);


    List<Ticket> findByIdIn(List<Long> ids);

}
