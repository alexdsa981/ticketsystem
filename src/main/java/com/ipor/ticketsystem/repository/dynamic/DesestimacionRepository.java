package com.ipor.ticketsystem.repository.dynamic;

import com.ipor.ticketsystem.model.dynamic.Desestimacion;
import com.ipor.ticketsystem.model.dynamic.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DesestimacionRepository extends JpaRepository<Desestimacion, Long> {

    @Query(value = "SELECT d.* FROM desestimacion d INNER JOIN ticket t ON d.id_ticket = t.id WHERE t.id_usuario = :idUsuario AND t.id_fase_ticket = 4", nativeQuery = true)
    List<Desestimacion> findAllByTicketUsuarioId(@Param("idUsuario") Long idUsuario);

    @Query(value = "SELECT d.* FROM desestimacion d INNER JOIN ticket t ON d.id_ticket = t.id WHERE t.id_fase_ticket = 4", nativeQuery = true)
    List<Desestimacion> findAllByTicketFaseID4();

    //numero total
    long count();

}
