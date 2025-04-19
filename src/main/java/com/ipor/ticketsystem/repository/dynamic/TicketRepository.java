package com.ipor.ticketsystem.repository.dynamic;

import com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    //obtener tickets por fase id
    List<Ticket> findByFaseTicketId(int faseId);
    //obtener tickets propios
    List<Ticket> findByUsuarioIdAndFaseTicketId(Long usuarioId, Long faseTicketId);

    @Query("SELECT MAX(CAST(SUBSTRING(t.codigoTicket, 4) AS int)) FROM Ticket t")
    Integer obtenerUltimoNumeroTicket();

    //para dashboard
    //numero total de ticket
    long count();

    //obtiene el numero de tickets por fase
    long countByFaseTicketNombre(String nombre);

    //dashboard admin conteo de tickets por fase
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(ft.nombre, " +
            "COALESCE(COUNT(t), 0)) " +
            "FROM FaseTicket ft LEFT JOIN Ticket t ON t.faseTicket = ft " +
            "GROUP BY ft.nombre " +
            "ORDER BY MIN(ft.id)")
    List<RecordFactorXConteo> findTicketCountByFase();

    //dashboard admin tickets creados por clasificación incidencia
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(ci.nombre, COUNT(t)) " +
            "FROM Ticket t INNER JOIN t.clasificacionIncidencia ci " +
            "GROUP BY ci.nombre")
    List<RecordFactorXConteo> findTicketCountByClasificacionIncidencia();


    //dashboard admin tickets por clasificacion urgencia
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(cu.nombre, COUNT(t)) " +
            "FROM Ticket t " +
            "INNER JOIN t.recepcion r " +
            "INNER JOIN r.clasificacionUrgencia cu " +
            "GROUP BY cu.nombre")
    List<RecordFactorXConteo> findTicketCountByClasificacionUrgencia();


    // Conteo de tickets por fase con filtro de fechas
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(ft.nombre, COALESCE(COUNT(t), 0)) " +
            "FROM FaseTicket ft LEFT JOIN Ticket t ON t.faseTicket = ft " +
            "WHERE (t.fecha BETWEEN :fechaInicio AND :fechaFin OR t IS NULL) " +
            "GROUP BY ft.nombre " +
            "ORDER BY MIN(ft.id)")
    List<RecordFactorXConteo> findTicketCountByFaseWithDates(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    // Conteo de tickets por clasificación de incidencia con filtro de fechas
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(ci.nombre, COUNT(t)) " +
            "FROM Ticket t INNER JOIN t.clasificacionIncidencia ci " +
            "WHERE t.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY ci.nombre")
    List<RecordFactorXConteo> findTicketCountByClasificacionIncidenciaWithDates(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    // Conteo de tickets por clasificación de urgencia con filtro de fechas
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(cu.nombre, COUNT(t)) " +
            "FROM Ticket t " +
            "INNER JOIN t.recepcion r " +
            "INNER JOIN r.clasificacionUrgencia cu " +
            "WHERE t.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY cu.nombre")
    List<RecordFactorXConteo> findTicketCountByClasificacionUrgenciaWithDates(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);


    @Query(value = "SELECT AVG(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(s.fecha, ' ', LEFT(s.hora, 8)) AS DATETIME))) " +
            "FROM servicio s " +
            "INNER JOIN ticket t ON t.id = s.id_ticket " +
            "INNER JOIN recepcion r ON t.id = r.id_ticket " +
            "WHERE TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(s.fecha, ' ', LEFT(s.hora, 8)) AS DATETIME) IS NOT NULL",
            nativeQuery = true)
    Double obtenerPromedioSegundosRS();

    @Query(value = "SELECT AVG(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME))) " +
            "FROM recepcion r " +
            "INNER JOIN ticket t ON t.id = r.id_ticket " +
            "WHERE TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME) IS NOT NULL",
            nativeQuery = true)
    Double obtenerPromedioSegundosTR();

    @Query(value = "SELECT AVG(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(s.fecha, ' ', LEFT(s.hora, 8)) AS DATETIME))) " +
            "FROM servicio s " +
            "INNER JOIN ticket t ON t.id = s.id_ticket " +
            "WHERE TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(s.fecha, ' ', LEFT(s.hora, 8)) AS DATETIME) IS NOT NULL",
            nativeQuery = true)
    Double obtenerPromedioSegundosTS();

}
