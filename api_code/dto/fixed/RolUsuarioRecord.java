package com.ipor.ticketsystem.api_code.dto.fixed;

import com.ipor.ticketsystem.model.fixed.RolUsuario;

public record RolUsuarioRecord(
        Long id,
        String nombre
) {
    public RolUsuarioRecord(RolUsuario rolUsuario){
        this(rolUsuario.getId(), rolUsuario.getNombre());
    }
}

//        Boolean PUEDE_CREAR_TICKET,
//        Boolean PUEDE_EDITAR_TICKET,
//        Boolean PUEDE_CANCELAR_TICKET,
//        Boolean PUEDE_VER_SUS_TICKET,
//        Boolean PUEDE_VER_HISTORIAL_TICKET,
//        Boolean PUEDE_VER_LISTA_TICKET,
//        Boolean PUEDE_ATENDER_TICKET,
//        Boolean PUEDE_CERRAR_TICKET,
//        Boolean PUEDE_MODIFICAR_TABLAS,
//        Boolean PUEDE_EXPORTAR_DATOS,
//        Boolean PUEDE_VER_ESTADISTICAS
