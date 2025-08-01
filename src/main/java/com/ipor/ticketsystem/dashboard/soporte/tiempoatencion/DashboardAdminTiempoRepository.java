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


    @Query(value = "SELECT SUM(DATEDIFF(SECOND, t.fecha_hora, r.fecha_hora)) " +
            "FROM atencion a " +
            "INNER JOIN recepcion r ON r.id_ticket = a.id_ticket " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "WHERE r.id_usuario = a.id_usuario " +
            "AND a.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Double obtenerSegundosTicketRecepcionConFecha(@Param("fechaInicio") LocalDate fechaInicio,
                                                  @Param("fechaFin") LocalDate fechaFin);


    @Query(value = "SELECT SUM(DATEDIFF(SECOND, r.fecha_hora, a.fecha_hora)) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "INNER JOIN recepcion r ON r.id_ticket = a.id_ticket " +
            "WHERE r.id_usuario = a.id_usuario " +
            "AND a.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Double obtenerSegundosRecepcionAtencionConFecha(@Param("fechaInicio") LocalDate fechaInicio,
                                                    @Param("fechaFin") LocalDate fechaFin);




    @Query(value = "SELECT SUM(DATEDIFF(SECOND, t.fecha_hora, a.fecha_hora)) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "INNER JOIN recepcion r ON r.id_ticket = a.id_ticket " +
            "WHERE a.id_usuario = r.id_usuario " +
            "AND a.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Double obtenerSegundosTicketAtencionConFecha(@Param("fechaInicio") LocalDate fechaInicio,
                                                 @Param("fechaFin") LocalDate fechaFin);




    @Query(value = "SELECT SUM(DATEDIFF(SECOND, de.fecha_hora_inicio, de.fecha_hora_fin)) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "INNER JOIN detalle_en_espera de ON de.id_ticket = a.id_ticket " +
            "WHERE de.id_clasificacion_espera = 1 " +
            "AND de.fecha_hora_inicio IS NOT NULL " +
            "AND de.fecha_hora_fin IS NOT NULL " +
            "AND a.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Double obtenerSegundosEsperaClasificacion1ConFecha(@Param("fechaInicio") LocalDate fechaInicio,
                                                       @Param("fechaFin") LocalDate fechaFin);




    @Query(value = "SELECT SUM(DATEDIFF(SECOND, de.fecha_hora_inicio, de.fecha_hora_fin)) " +
            "FROM atencion a " +
            "INNER JOIN ticket t ON t.id = a.id_ticket " +
            "INNER JOIN detalle_en_espera de ON de.id_ticket = a.id_ticket " +
            "WHERE de.id_clasificacion_espera != 1 " +
            "AND de.fecha_hora_inicio IS NOT NULL " +
            "AND de.fecha_hora_fin IS NOT NULL " +
            "AND a.fecha BETWEEN :fechaInicio AND :fechaFin",
            nativeQuery = true)
    Double obtenerSegundosEsperaClasificacionNo1ConFecha(@Param("fechaInicio") LocalDate fechaInicio,
                                                         @Param("fechaFin") LocalDate fechaFin);


    @Query(value = """
            SELECT SUM(DATEDIFF(SECOND,
                t.fecha_hora,
                a.fecha_hora
            ))
            FROM atencion a
            INNER JOIN ticket t ON t.id = a.id_ticket
            INNER JOIN recepcion r ON r.id_ticket = a.id_ticket
            WHERE r.id_usuario = :idUsuario AND r.id_usuario = a.id_usuario
            AND t.fecha_hora IS NOT NULL
            AND a.fecha_hora IS NOT NULL
            AND a.fecha BETWEEN :fechaInicio AND :fechaFin
    """, nativeQuery = true)
    Double obtenerTiempoAtencionEnSegundosPorUsuario(
            @Param("idUsuario") Long idUsuario,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );



    @Query(value = """
            SELECT SUM(DATEDIFF(SECOND,
                de.fecha_hora_inicio,
                de.fecha_hora_fin
            ))
            FROM atencion a
            INNER JOIN ticket t ON t.id = a.id_ticket
            INNER JOIN recepcion r ON r.id_ticket = a.id_ticket
            INNER JOIN detalle_en_espera de ON de.id_ticket = a.id_ticket
            WHERE r.id_usuario = :idUsuario AND r.id_usuario = a.id_usuario
            AND de.fecha_hora_inicio IS NOT NULL
            AND de.fecha_hora_fin IS NOT NULL
            AND a.fecha BETWEEN :fechaInicio AND :fechaFin
    """, nativeQuery = true)
    Double obtenerTiempoEsperaEnSegundosPorUsuario(
            @Param("idUsuario") Long idUsuario,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );


}
