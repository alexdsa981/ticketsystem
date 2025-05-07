package com.ipor.ticketsystem.repository.dynamic;

import com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo;
import com.ipor.ticketsystem.model.dynamic.Ticket;
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


    /*
    INFORMACION PARA DASHBOARD SOPORTE
    */

    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(ft.nombre, " +
            "COALESCE(COUNT(t), 0)) " +
            "FROM FaseTicket ft LEFT JOIN Ticket t ON t.faseTicket = ft " +
            "GROUP BY ft.nombre " +
            "ORDER BY MIN(ft.id)")
    List<RecordFactorXConteo> findTicketCountByFase();


    // Conteo de tickets por fase con filtro de fechas
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(ft.nombre, COALESCE(COUNT(t), 0)) " +
            "FROM FaseTicket ft LEFT JOIN Ticket t ON t.faseTicket = ft AND t.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY ft.nombre " +
            "ORDER BY MIN(ft.id)")
    List<RecordFactorXConteo> findTicketCountByFaseWithDates(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    /*
    INFORMACION PARA DASHBOARD ADMINISTRADOR
    */

    //CONTEO POR INCIDENCIA SIN Y CON FECHA
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(ti.nombre, COUNT(a)) " +
            "FROM Atencion a INNER JOIN a.tipoIncidencia ti " +
            "GROUP BY ti.nombre")
    List<RecordFactorXConteo> findTicketCountByTipoIncidencia();
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(ti.nombre, COUNT(a)) " +
            "FROM Atencion a INNER JOIN a.tipoIncidencia ti " +
            "WHERE (a.fecha BETWEEN :fechaInicio AND :fechaFin) " +
            "GROUP BY ti.nombre")
    List<RecordFactorXConteo> findTicketCountByTipoIncidenciaWithDates(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);


    //CONTEO POR URGENCIA SIN Y CON FECHA
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(cu.nombre, COUNT(a)) " +
            "FROM Atencion a " +
            "INNER JOIN a.clasificacionUrgencia cu " +
            "GROUP BY cu.nombre")
    List<RecordFactorXConteo> findTicketCountByClasificacionUrgencia();
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(cu.nombre, COUNT(a)) " +
            "FROM Atencion a " +
            "INNER JOIN a.clasificacionUrgencia cu " +
            "WHERE a.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY cu.nombre")
    List<RecordFactorXConteo> findTicketCountByClasificacionUrgenciaWithDates(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);




    //CONTEO POR AREA SIN Y CON FECHA
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(aa.nombre, COUNT(a)) " +
            "FROM Atencion a " +
            "INNER JOIN a.areaAtencion aa " +
            "GROUP BY aa.nombre")
    List<RecordFactorXConteo> findTicketCountByArea();
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(aa.nombre, COUNT(a)) " +
            "FROM Atencion a " +
            "INNER JOIN a.areaAtencion aa " +
            "WHERE a.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY aa.nombre")
    List<RecordFactorXConteo> findTicketCountByAreaWithDates(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);




    //MEDICION DE TIEMPO SIN Y CON FECHA
    @Query(value = "SELECT AVG(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME))) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "INNER JOIN recepcion r ON t.id = r.id_ticket " +
            "WHERE TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME) IS NOT NULL",
            nativeQuery = true)
    Double obtenerPromedioSegundosRS();
    @Query(value = "SELECT AVG(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME))) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "INNER JOIN recepcion r ON t.id = r.id_ticket " +
            "WHERE TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND t.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Double obtenerPromedioSegundosRSConFecha(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);
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
            "TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME))) " +
            "FROM recepcion r " +
            "INNER JOIN ticket t ON t.id = r.id_ticket " +
            "WHERE TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND t.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Double obtenerPromedioSegundosTRConFecha(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);
    @Query(value = "SELECT AVG(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME))) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "WHERE TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME) IS NOT NULL",
            nativeQuery = true)
    Double obtenerPromedioSegundosTS();
    @Query(value = "SELECT AVG(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME))) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "WHERE TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND t.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Double obtenerPromedioSegundosTSConFecha(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);


    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.fecha BETWEEN :inicio AND :fin")
    long countTicketsByFechaBetween(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);
}
