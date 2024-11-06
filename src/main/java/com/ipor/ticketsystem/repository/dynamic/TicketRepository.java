package com.ipor.ticketsystem.repository.dynamic;

import com.ipor.ticketsystem.model.dto.otros.graficos.RecordConteoTickets_Fase;
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

    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordConteoTickets_Fase(ft.nombre, COUNT(t)) " +
            "FROM Ticket t JOIN t.faseTicket ft " +
            "GROUP BY ft.nombre " +
            "ORDER BY MIN(ft.id)")
    List<RecordConteoTickets_Fase> findTicketCountByFase();
}
