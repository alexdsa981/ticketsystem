package com.ipor.ticketsystem.repository.dynamic;

import com.ipor.ticketsystem.model.dynamic.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificacionesRepository extends JpaRepository<Notificacion, Long> {
    @Query("SELECT n FROM Notificacion n WHERE n.ticket.id = :ticketId")
    List<Notificacion> BuscarPorIdTicket(@Param("ticketId") Long ticketId);

    @Query("SELECT n FROM Notificacion n WHERE n.usuario.id = :usuarioId")
    List<Notificacion> BuscarPorIdUsuario(@Param("usuarioId") Long usuarioId);

    @Query(value = "SELECT TOP 15 * FROM notificacion n WHERE n.id_usuario = :usuarioId ORDER BY n.fecha DESC, n.hora DESC", nativeQuery = true)
    List<Notificacion> findTop15ByUsuarioId(@Param("usuarioId") Long usuarioId);


}
