package com.ipor.ticketsystem.repository.dynamic;

import com.ipor.ticketsystem.model.dynamic.ArchivoAdjuntoAtencion;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjuntoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArchivoAdjuntoAtencionRepository extends JpaRepository<ArchivoAdjuntoAtencion, Long> {
    @Query("SELECT a FROM ArchivoAdjuntoAtencion a WHERE a.atencion.id = :atencionId")
    List<ArchivoAdjuntoAtencion> BuscarPorIdTicket(@Param("atencionId") Long atencionId);

}
