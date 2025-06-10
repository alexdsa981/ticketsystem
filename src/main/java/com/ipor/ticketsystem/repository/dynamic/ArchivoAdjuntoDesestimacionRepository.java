package com.ipor.ticketsystem.repository.dynamic;

import com.ipor.ticketsystem.model.dynamic.ArchivoAdjuntoAtencion;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjuntoDesestimacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArchivoAdjuntoDesestimacionRepository extends JpaRepository<ArchivoAdjuntoDesestimacion, Long> {

}
