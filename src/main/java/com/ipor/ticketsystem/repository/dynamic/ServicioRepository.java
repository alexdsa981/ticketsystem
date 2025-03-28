package com.ipor.ticketsystem.repository.dynamic;

import com.ipor.ticketsystem.model.dynamic.Recepcion;
import com.ipor.ticketsystem.model.dynamic.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    // Consulta para obtener los 300 servicios más recientes según idUsuario y fase del ticket
    @Query(value = "SELECT TOP 300 s.* FROM servicio s INNER JOIN ticket t ON s.id_ticket = t.id WHERE t.id_usuario = :idUsuario AND t.id_fase_ticket = 3 ORDER BY s.fecha DESC", nativeQuery = true)
    List<Servicio> findAllByTicketUsuarioId(@Param("idUsuario") Long idUsuario);

    // Consulta para obtener los 300 servicios más recientes según la fase del ticket
    @Query(value = "SELECT TOP 300 s.* FROM servicio s INNER JOIN ticket t ON s.id_ticket = t.id WHERE t.id_fase_ticket = 3 ORDER BY s.fecha DESC", nativeQuery = true)
    List<Servicio> findAllByTicketFaseID3();

    //numero total
    long count();

}
