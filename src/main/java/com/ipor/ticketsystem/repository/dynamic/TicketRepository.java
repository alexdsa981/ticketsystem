package com.ipor.ticketsystem.repository.dynamic;

import com.ipor.ticketsystem.model.dto.otros.graficos.RecordConteoTicketxFactor;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    //obtener tickets por fase id
    List<Ticket> findByFaseTicketId(int faseId);
    //obtener tickets propios
    List<Ticket> findByUsuarioIdAndFaseTicketId(Long usuarioId, Long faseTicketId);

    //para dashboard
    //numero total de ticket
    long count();
    long countByFaseTicketNombre(String nombre);

    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordConteoTicketxFactor(ft.nombre, " +
            "COALESCE(COUNT(t), 0)) " +
            "FROM FaseTicket ft LEFT JOIN Ticket t ON t.faseTicket = ft " +
            "GROUP BY ft.nombre " +
            "ORDER BY MIN(ft.id)")
    List<RecordConteoTicketxFactor> findTicketCountByFase();

    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordConteoTicketxFactor(ci.nombre, COUNT(t)) " +
            "FROM Ticket t INNER JOIN t.clasificacionIncidencia ci " +
            "GROUP BY ci.nombre")
    List<RecordConteoTicketxFactor> findTicketCountByClasificacionIncidencia();

    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordConteoTicketxFactor(cu.nombre, COUNT(t)) " +
            "FROM Ticket t " +
            "INNER JOIN t.recepcion r " +
            "INNER JOIN r.clasificacionUrgencia cu " +
            "WHERE t.faseTicket.id = 3 " +
            "GROUP BY cu.nombre")
    List<RecordConteoTicketxFactor> findTicketCountByClasificacionUrgencia();





}
