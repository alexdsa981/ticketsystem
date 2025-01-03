package com.ipor.ticketsystem.repository.dynamic;

import com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    //obtener tickets por fase id
    List<Ticket> findByFaseTicketId(int faseId);
    //obtener tickets propios
    List<Ticket> findByUsuarioIdAndFaseTicketId(Long usuarioId, Long faseTicketId);


    @Query(value = "SELECT DISTINCT t.* " +
            "FROM ticket t " +
            "INNER JOIN tipo_componente_adjunto tca ON t.id = tca.id_ticket " +
            "WHERE tca.aprobado IS NOT NULL",
            nativeQuery = true)
    List<Ticket> findTicketsConAdjuntosNoNulos();




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


    //dashboard direccion
    @Query("SELECT new com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo(tc.nombre, SUM(tca.cantidad)) " +
            "FROM TipoComponenteAdjunto tca " +
            "INNER JOIN tca.tipoComponente tc " +
            "WHERE tca.aprobado = true " +
            "GROUP BY tc.nombre")
    List<RecordFactorXConteo> findGroupedByTipoComponenteAprobado();






}
