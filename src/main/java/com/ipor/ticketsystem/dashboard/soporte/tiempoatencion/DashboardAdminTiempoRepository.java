package com.ipor.ticketsystem.dashboard.soporte.tiempoatencion;

import com.ipor.ticketsystem.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface DashboardAdminTiempoRepository extends JpaRepository<Ticket, Long> {


    @Query(value = "SELECT COUNT(*) " +
            "FROM atencion a " +
            "WHERE a.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Long contarTicketsAtendidosEnRango(@Param("fechaInicio") LocalDate fechaInicio,
                                       @Param("fechaFin") LocalDate fechaFin);

    @Query(value = "SELECT COUNT(*) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "INNER JOIN recepcion r ON r.id_ticket = a.id_ticket " +
            "WHERE r.id_usuario = a.id_usuario " +
            "AND a.id_usuario = :idUsuario " +
            "AND a.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Long contarTicketsAtendidosPorUsuarioEnRango(@Param("idUsuario") Long idUsuario,
                                                 @Param("fechaInicio") LocalDate fechaInicio,
                                                 @Param("fechaFin") LocalDate fechaFin);


    @Query(value = "SELECT SUM(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME))) " +
            "FROM atencion a " +
            "INNER JOIN recepcion r ON r.id_ticket = a.id_ticket " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "WHERE TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND r.id_usuario = a.id_usuario " +
            "AND a.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Double obtenerSegundosTicketRecepcionConFecha(@Param("fechaInicio") LocalDate fechaInicio,
                                                  @Param("fechaFin") LocalDate fechaFin);


    @Query(value = "SELECT SUM(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME))) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "INNER JOIN recepcion r ON r.id_ticket =  a.id_ticket " +
            "WHERE TRY_CAST(CONCAT(r.fecha, ' ', LEFT(r.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND r.id_usuario = a.id_usuario " +
            "AND a.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Double obtenerSegundosRecepcionAtencionConFecha(@Param("fechaInicio") LocalDate fechaInicio,
                                                    @Param("fechaFin") LocalDate fechaFin);




    @Query(value = "SELECT SUM(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME))) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "INNER JOIN recepcion r ON r.id_ticket = a.id_ticket " +
            "WHERE TRY_CAST(CONCAT(t.fecha, ' ', LEFT(t.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(a.fecha, ' ', LEFT(a.hora, 8)) AS DATETIME) IS NOT NULL " +
            "AND a.id_usuario = r.id_usuario " +
            "AND a.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Double obtenerSegundosTicketAtencionConFecha(@Param("fechaInicio") LocalDate fechaInicio,
                                                 @Param("fechaFin") LocalDate fechaFin);




    @Query(value = "SELECT SUM(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(de.fecha_inicio, ' ', LEFT(de.hora_inicio, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(de.fecha_fin, ' ', LEFT(de.hora_fin, 8)) AS DATETIME))) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "INNER JOIN detalle_en_espera de ON de.id_ticket = a.id_ticket " +
            "WHERE de.id_clasificacion_espera = 1 " +
            "AND TRY_CAST(CONCAT(de.fecha_inicio, ' ', LEFT(de.hora_inicio, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(de.fecha_fin, ' ', LEFT(de.hora_fin, 8)) AS DATETIME) IS NOT NULL " +
            "AND a.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Double obtenerSegundosEsperaClasificacion1ConFecha(@Param("fechaInicio") LocalDate fechaInicio,
                                                       @Param("fechaFin") LocalDate fechaFin);




    @Query(value = "SELECT AVG(DATEDIFF(SECOND, " +
            "TRY_CAST(CONCAT(de.fecha_inicio, ' ', LEFT(de.hora_inicio, 8)) AS DATETIME), " +
            "TRY_CAST(CONCAT(de.fecha_fin, ' ', LEFT(de.hora_fin, 8)) AS DATETIME))) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "INNER JOIN detalle_en_espera de ON de.id_ticket = a.id_ticket " +
            "WHERE de.id_clasificacion_espera != 1 " +
            "AND TRY_CAST(CONCAT(de.fecha_inicio, ' ', LEFT(de.hora_inicio, 8)) AS DATETIME) IS NOT NULL " +
            "AND TRY_CAST(CONCAT(de.fecha_fin, ' ', LEFT(de.hora_fin, 8)) AS DATETIME) IS NOT NULL " +
            "AND a.fecha BETWEEN :fechaInicio AND :fechaFin",
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
                AND a.fecha BETWEEN :fechaInicio AND :fechaFin
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
                FROM atencion a
                INNER JOIN ticket t ON t.id = a.id_ticket
                INNER JOIN recepcion r ON r.id_ticket = a.id_ticket
                INNER JOIN detalle_en_espera de ON de.id_ticket = a.id_ticket
                WHERE r.id_usuario = :idUsuario AND r.id_usuario = a.id_usuario
                AND TRY_CAST(CONCAT(de.fecha_inicio, ' ', LEFT(de.hora_inicio, 8)) AS DATETIME) IS NOT NULL
                AND TRY_CAST(CONCAT(de.fecha_fin, ' ', LEFT(de.hora_fin, 8)) AS DATETIME) IS NOT NULL
                AND a.fecha BETWEEN :fechaInicio AND :fechaFin
            """, nativeQuery = true)
    Double obtenerTiempoEsperaEnSegundosPorUsuario(
            @Param("idUsuario") Long idUsuario,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

}
