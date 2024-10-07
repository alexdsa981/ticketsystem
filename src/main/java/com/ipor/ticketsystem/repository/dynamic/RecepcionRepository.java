package com.ipor.ticketsystem.repository.dynamic;

import com.ipor.ticketsystem.model.dynamic.Recepcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecepcionRepository extends JpaRepository<Recepcion, Long> {
    @Query(value = "SELECT r.* FROM recepcion r INNER JOIN ticket t ON r.id_ticket = t.id WHERE t.id_usuario = :idUsuario and t.id_fase_ticket= 2", nativeQuery = true)
    List<Recepcion> findAllByTicketUsuarioId(@Param("idUsuario") Long idUsuario);

    @Query(value = "SELECT r.* FROM recepcion r INNER JOIN ticket t ON r.id_ticket = t.id WHERE t.id_fase_ticket= 2", nativeQuery = true)
    List<Recepcion> findAllByTicketFaseID2();

}
