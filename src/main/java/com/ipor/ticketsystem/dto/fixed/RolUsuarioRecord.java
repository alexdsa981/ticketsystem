package com.ipor.ticketsystem.dto.fixed;


public record RolUsuarioRecord(
        Long id,
        String nombre,
        Boolean PUEDE_CREAR_TICKET,
        Boolean PUEDE_EDITAR_TICKET,
        Boolean PUEDE_CANCELAR_TICKET,
        Boolean PUEDE_VER_SUS_TICKET,
        Boolean PUEDE_VER_HISTORIAL_TICKET,
        Boolean PUEDE_VER_LISTA_TICKET,
        Boolean PUEDE_ATENDER_TICKET,
        Boolean PUEDE_CERRAR_TICKET,
        Boolean PUEDE_MODIFICAR_TABLAS,
        Boolean PUEDE_EXPORTAR_DATOS,
        Boolean PUEDE_VER_ESTADISTICAS
) {
}
