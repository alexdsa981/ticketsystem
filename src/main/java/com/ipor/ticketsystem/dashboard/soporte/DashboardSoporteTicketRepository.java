package com.ipor.ticketsystem.dashboard.soporte;

import com.ipor.ticketsystem.dashboard.RecordFactorXConteo;
import com.ipor.ticketsystem.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DashboardSoporteTicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT new com.ipor.ticketsystem.dashboard.RecordFactorXConteo(ft.nombre, " +
            "COALESCE(COUNT(t), 0)) " +
            "FROM FaseTicket ft LEFT JOIN Ticket t ON t.faseTicket = ft " +
            "GROUP BY ft.nombre " +
            "ORDER BY MIN(ft.id)")
    List<RecordFactorXConteo> findTicketCountByFase();


    // Conteo de tickets por fase con filtro de fechas
    @Query("""
    SELECT new com.ipor.ticketsystem.dashboard.RecordFactorXConteo(
        ft.nombre,
        COUNT(
            CASE
                WHEN ft.nombre = 'Cerrado - Atendido' 
                     AND a.fecha BETWEEN :fechaInicio AND :fechaFin THEN 1
                WHEN ft.nombre = 'Desestimado' 
                     AND d.fecha BETWEEN :fechaInicio AND :fechaFin THEN 1
                WHEN ft.nombre NOT IN ('Cerrado - Atendido', 'Desestimado') 
                     AND t.id IS NOT NULL THEN 1
                ELSE NULL
            END
        )
    )
    FROM FaseTicket ft
    LEFT JOIN Ticket t ON t.faseTicket = ft
    LEFT JOIN t.atencion a
    LEFT JOIN t.desestimacion d
    GROUP BY ft.nombre, ft.id
    ORDER BY ft.id
    """)
    List<RecordFactorXConteo> findTicketCountByFaseWithDates(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    //top 5 tickets que no estan atendidos ni desestimados ordenados por más recientes:
    List<Ticket> findTop5ByFaseTicket_IdNotInOrderByFechaDescHoraDesc(List<Long> ids);

    @Query(
            value = """
        SELECT
            t.id,
            t.codigo_ticket AS codigoTicket,
            t.descripcion,
            ft.nombre AS nombreFase,
            u.nombre AS nombreUsuario,
            COALESCE(
                (SELECT TOP 1 r.fecha FROM recepcion r WHERE r.id_ticket = t.id ORDER BY r.fecha DESC),
                (SELECT TOP 1 e.fecha_inicio FROM detalle_en_espera e WHERE e.id_ticket = t.id ORDER BY e.fecha_inicio DESC),
                t.fecha
            ) AS ultimaFecha
        FROM ticket t
        JOIN fase_ticket ft ON ft.id = t.id_fase_ticket
        JOIN usuario u ON u.id = t.id_usuario
        WHERE t.id_fase_ticket IN (1, 2, 5)
        AND DATEDIFF(DAY,
            COALESCE(
                (SELECT TOP 1 r.fecha FROM recepcion r WHERE r.id_ticket = t.id ORDER BY r.fecha DESC),
                (SELECT TOP 1 e.fecha_inicio FROM detalle_en_espera e WHERE e.id_ticket = t.id ORDER BY e.fecha_inicio DESC),
                t.fecha
            ),
            GETDATE()
        ) >= 3
        ORDER BY ultimaFecha ASC
        """,
            nativeQuery = true
    )
    List<TicketInactivoProjection> obtenerTicketsInactivosMasAntiguos();
}
