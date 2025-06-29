package com.ipor.ticketsystem.repository.dynamic;

import com.ipor.ticketsystem.model.dto.otros.TicketInactivoProjection;
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
    @Query("""
    SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(
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


    /*
    INFORMACION PARA DASHBOARD ADMINISTRADOR
    */


    //CATEGORIA INCIDENCIA SIN Y CON FECHA
// CATEGORÍA INCIDENCIA SIN FECHA
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(ci.nombre, COUNT(a)) " +
            "FROM Atencion a " +
            "INNER JOIN a.tipoIncidencia ti " +
            "INNER JOIN ti.subCategoriaIncidencia sci " +
            "INNER JOIN sci.categoriaIncidencia ci " +
            "GROUP BY ci.nombre " +
            "ORDER BY COUNT(a) DESC")
    List<RecordFactorXConteo> findTicketCountByCATIncidencia();

    // CATEGORÍA INCIDENCIA CON FECHA
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(ci.nombre, COUNT(a)) " +
            "FROM Atencion a " +
            "INNER JOIN a.tipoIncidencia ti " +
            "INNER JOIN ti.subCategoriaIncidencia sci " +
            "INNER JOIN sci.categoriaIncidencia ci " +
            "WHERE a.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY ci.nombre " +
            "ORDER BY COUNT(a) DESC")
    List<RecordFactorXConteo> findTicketCountByCATIncidenciaWithDates(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);


    // SUBCATEGORÍA INCIDENCIA SIN FECHA
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(sci.nombre, COUNT(a)) " +
            "FROM Atencion a " +
            "INNER JOIN a.tipoIncidencia ti " +
            "INNER JOIN ti.subCategoriaIncidencia sci " +
            "GROUP BY sci.nombre " +
            "ORDER BY COUNT(a) DESC")
    List<RecordFactorXConteo> findTicketCountBySubCATIncidencia();

    // SUBCATEGORÍA INCIDENCIA CON FECHA
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(sci.nombre, COUNT(a)) " +
            "FROM Atencion a " +
            "INNER JOIN a.tipoIncidencia ti " +
            "INNER JOIN ti.subCategoriaIncidencia sci " +
            "WHERE a.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY sci.nombre " +
            "ORDER BY COUNT(a) DESC")
    List<RecordFactorXConteo> findTicketCountBySubCATIncidenciaWithDates(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);



    //CONTEO POR INCIDENCIA SIN Y CON FECHA
// CONTEO POR INCIDENCIA SIN FECHA
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(" +
            "CONCAT(sci.nombre, ' - ',ti.nombre), COUNT(a)) " +
            "FROM Atencion a " +
            "INNER JOIN a.tipoIncidencia ti " +
            "INNER JOIN ti.subCategoriaIncidencia sci " +
            "GROUP BY ti.nombre, sci.nombre " +
            "ORDER BY COUNT(a) DESC")
    List<RecordFactorXConteo> findTicketCountByTipoIncidencia();

    // CONTEO POR INCIDENCIA CON FECHA
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(" +
            "CONCAT(sci.nombre, ' - ',ti.nombre), COUNT(a)) " +
            "FROM Atencion a " +
            "INNER JOIN a.tipoIncidencia ti " +
            "INNER JOIN ti.subCategoriaIncidencia sci " +
            "WHERE a.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY ti.nombre, sci.nombre " +
            "ORDER BY COUNT(a) DESC")
    List<RecordFactorXConteo> findTicketCountByTipoIncidenciaWithDates(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);


    //CONTEO POR URGENCIA SIN Y CON FECHA
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(cu.nombre, COUNT(a)) " +
            "FROM Atencion a " +
            "INNER JOIN a.clasificacionUrgencia cu " +
            "GROUP BY cu.nombre " +
            "ORDER BY COUNT(a) DESC")
    List<RecordFactorXConteo> findTicketCountByClasificacionUrgencia();
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(cu.nombre, COUNT(a)) " +
            "FROM Atencion a " +
            "INNER JOIN a.clasificacionUrgencia cu " +
            "WHERE a.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY cu.nombre " +
            "ORDER BY COUNT(a) DESC")
    List<RecordFactorXConteo> findTicketCountByClasificacionUrgenciaWithDates(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);



    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(se.nombre, COUNT(a)) " +
            "FROM Atencion a " +
            "INNER JOIN a.areaAtencion aa " +
            "INNER JOIN aa.sede se " +
            "GROUP BY se.nombre " +
            "ORDER BY COUNT(a) DESC")
    List<RecordFactorXConteo> findTicketCountBySede();

    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(se.nombre, COUNT(a)) " +
            "FROM Atencion a " +
            "INNER JOIN a.areaAtencion aa " +
            "INNER JOIN aa.sede se " +
            "WHERE a.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY se.nombre " +
            "ORDER BY COUNT(a) DESC")
    List<RecordFactorXConteo> findTicketCountBySedeWithDates(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);




    //CONTEO POR AREA SIN Y CON FECHA
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(" +
            "CONCAT(se.nombre, ' - ', aa.nombre), COUNT(a)) " +
            "FROM Atencion a " +
            "INNER JOIN a.areaAtencion aa " +
            "INNER JOIN aa.sede se " +
            "GROUP BY se.nombre, aa.nombre " +
            "ORDER BY COUNT(a) DESC")
    List<RecordFactorXConteo> findTicketCountByArea();

    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(" +
            "CONCAT(se.nombre, ' - ', aa.nombre), COUNT(a)) " +
            "FROM Atencion a " +
            "INNER JOIN a.areaAtencion aa " +
            "INNER JOIN aa.sede se " +
            "WHERE a.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY se.nombre, aa.nombre " +
            "ORDER BY COUNT(a) DESC")
    List<RecordFactorXConteo> findTicketCountByAreaWithDates(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);


    @Query(value = "SELECT COUNT(*) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket",
            nativeQuery = true)
    Long contarTicketsAtendidos();

    @Query(value = "SELECT COUNT(*) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "WHERE t.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Long contarTicketsAtendidosEnRango(@Param("fechaInicio") LocalDate fechaInicio,
                                       @Param("fechaFin") LocalDate fechaFin);


    @Query(value = "SELECT COUNT(*) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "WHERE a.id_usuario = :idUsuario " +
            "AND t.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Long contarTicketsAtendidosPorUsuarioEnRango(@Param("idUsuario") Long idUsuario,
                                                 @Param("fechaInicio") LocalDate fechaInicio,
                                                 @Param("fechaFin") LocalDate fechaFin);




    @Query(value = "SELECT SUM(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME))) " +
            "FROM recepcion r " +
            "INNER JOIN ticket t ON t.id = r.id_ticket " +
            "WHERE TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME) IS NOT NULL",
            nativeQuery = true)
    Double obtenerSegundosTicketRecepcion();
    @Query(value = "SELECT SUM(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME))) " +
            "FROM recepcion r " +
            "INNER JOIN ticket t ON t.id = r.id_ticket " +
            "WHERE TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND t.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Double obtenerSegundosTicketRecepcionConFecha(@Param("fechaInicio") LocalDate fechaInicio,
                                                  @Param("fechaFin") LocalDate fechaFin);




    @Query(value = "SELECT SUM(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME))) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "INNER JOIN recepcion r ON t.id = r.id_ticket " +
            "WHERE TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME) IS NOT NULL",
            nativeQuery = true)
    Double obtenerSegundosRecepcionAtencion();
    @Query(value = "SELECT SUM(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME))) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "INNER JOIN recepcion r ON t.id = r.id_ticket " +
            "WHERE TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND t.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Double obtenerSegundosRecepcionAtencionConFecha(@Param("fechaInicio") LocalDate fechaInicio,
                                                    @Param("fechaFin") LocalDate fechaFin);


    @Query(value = "SELECT SUM(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME))) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "WHERE TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME) IS NOT NULL",
            nativeQuery = true)
    Double obtenerSegundosTicketAtencion();
    @Query(value = "SELECT SUM(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME))) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "WHERE TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND t.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Double obtenerSegundosTicketAtencionConFecha(@Param("fechaInicio") LocalDate fechaInicio,
                                                 @Param("fechaFin") LocalDate fechaFin);




    @Query(value = "SELECT SUM(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(de.fecha_inicio, ' ', LEFT(de.hora_inicio, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(de.fecha_fin, ' ', LEFT(de.hora_fin, 8)) AS DATETIME))) " +
            "FROM detalle_en_espera de " +
            "INNER JOIN atencion a ON a.id_ticket = de.id_ticket " +
            "WHERE de.id_clasificacion_espera = 1 " +
            "AND TRY_CAST(CONCAT(de.fecha_inicio, ' ', LEFT(de.hora_inicio, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(de.fecha_fin, ' ', LEFT(de.hora_fin, 8)) AS DATETIME) IS NOT NULL",
            nativeQuery = true)
    Double obtenerSegundosEsperaClasificacion1();
    @Query(value = "SELECT SUM(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(de.fecha_inicio, ' ', LEFT(de.hora_inicio, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(de.fecha_fin, ' ', LEFT(de.hora_fin, 8)) AS DATETIME))) " +
            "FROM detalle_en_espera de " +
            "INNER JOIN atencion a ON a.id_ticket = de.id_ticket " +
            "WHERE de.id_clasificacion_espera = 1 " +
            "AND TRY_CAST(CONCAT(de.fecha_inicio, ' ', LEFT(de.hora_inicio, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(de.fecha_fin, ' ', LEFT(de.hora_fin, 8)) AS DATETIME) IS NOT NULL " +
            "AND de.fecha_inicio BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Double obtenerSegundosEsperaClasificacion1ConFecha(@Param("fechaInicio") LocalDate fechaInicio,
                                                       @Param("fechaFin") LocalDate fechaFin);



    @Query(value = "SELECT AVG(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(de.fecha_inicio, ' ', LEFT(de.hora_inicio, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(de.fecha_fin, ' ', LEFT(de.hora_fin, 8)) AS DATETIME))) " +
            "FROM detalle_en_espera de " +
            "INNER JOIN atencion a ON a.id_ticket = de.id_ticket " +
            "WHERE de.id_clasificacion_espera != 1 " +
            "AND TRY_CAST(CONCAT(de.fecha_inicio, ' ', LEFT(de.hora_inicio, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(de.fecha_fin, ' ', LEFT(de.hora_fin, 8)) AS DATETIME) IS NOT NULL",
            nativeQuery = true)
    Double obtenerSegundosEsperaClasificacionNo1();
    @Query(value = "SELECT AVG(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(de.fecha_inicio, ' ', LEFT(de.hora_inicio, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(de.fecha_fin, ' ', LEFT(de.hora_fin, 8)) AS DATETIME))) " +
            "FROM detalle_en_espera de " +
            "INNER JOIN atencion a ON a.id_ticket = de.id_ticket " +
            "WHERE de.id_clasificacion_espera != 1 " +
            "AND TRY_CAST(CONCAT(de.fecha_inicio, ' ', LEFT(de.hora_inicio, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(de.fecha_fin, ' ', LEFT(de.hora_fin, 8)) AS DATETIME) IS NOT NULL " +
            "AND de.fecha_inicio BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Double obtenerSegundosEsperaClasificacionNo1ConFecha(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);



    @Query(value = """
    SELECT SUM(DATEDIFF(SECOND,
        TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME),
        TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME)
    ))
    FROM atencion a
    INNER JOIN ticket t ON t.id = a.id_ticket
    INNER JOIN recepcion r ON r.id_ticket = a.id_ticket
    WHERE r.id_usuario = :idUsuario AND r.id_usuario = a.id_usuario
    AND TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME) IS NOT NULL
    AND TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME) IS NOT NULL
    AND t.fecha BETWEEN :fechaInicio AND :fechaFin
""", nativeQuery = true)
    Double obtenerTiempoAtencionEnSegundosPorUsuario(
            @Param("idUsuario") Long idUsuario,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );






    @Query(value = """
    SELECT SUM(DATEDIFF(SECOND,
        TRY_CAST(CONCAT(de.fecha_inicio, ' ', LEFT(de.hora_inicio, 8)) AS DATETIME),
        TRY_CAST(CONCAT(de.fecha_fin, ' ', LEFT(de.hora_fin, 8)) AS DATETIME)
    ))
    FROM detalle_en_espera de
    INNER JOIN atencion a ON a.id_ticket = de.id_ticket
    INNER JOIN recepcion r ON r.id_ticket = a.id_ticket
    WHERE r.id_usuario = :idUsuario AND r.id_usuario = a.id_usuario
    AND TRY_CAST(CONCAT(de.fecha_inicio, ' ', LEFT(de.hora_inicio, 8)) AS DATETIME) IS NOT NULL
    AND TRY_CAST(CONCAT(de.fecha_fin, ' ', LEFT(de.hora_fin, 8)) AS DATETIME) IS NOT NULL
    AND de.fecha_inicio BETWEEN :fechaInicio AND :fechaFin
""", nativeQuery = true)
    Double obtenerTiempoEsperaEnSegundosPorUsuario(
            @Param("idUsuario") Long idUsuario,
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

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.fecha BETWEEN :inicio AND :fin")
    long countTicketsByFechaBetween(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);
}
