package com.ipor.ticketsystem.repository.dynamic;

import com.ipor.ticketsystem.model.dynamic.Atencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AtencionRepository extends JpaRepository<Atencion, Long> {

    // Consulta para obtener los 300 atencions más recientes según idUsuario y fase del ticket
    @Query(value = "SELECT TOP 300 a.* FROM atencion a INNER JOIN ticket t ON a.id_ticket = t.id WHERE t.id_usuario = :idUsuario AND t.id_fase_ticket = 3 ORDER BY a.fecha DESC, a.hora DESC", nativeQuery = true)
    List<Atencion> findAllByTicketUsuarioId(@Param("idUsuario") Long idUsuario);

    // Consulta para obtener los 300 atencions más recientes según la fase del ticket
    @Query(value = "SELECT TOP 300 a.* FROM atencion a INNER JOIN ticket t ON a.id_ticket = t.id WHERE t.id_fase_ticket = 3 ORDER BY a.fecha DESC, a.hora DESC", nativeQuery = true)
    List<Atencion> findAllByTicketFaseID3();

    //numero total
    long count();

    @Query("SELECT COUNT(a) FROM Atencion a WHERE a.fecha BETWEEN :inicio AND :fin")
    long countAtendidosByFechaBetween(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);
}
