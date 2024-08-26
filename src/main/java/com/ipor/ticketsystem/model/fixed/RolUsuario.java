package com.ipor.ticketsystem.model.fixed;

import com.ipor.ticketsystem.dto.fixed.RolUsuarioRecord;
import com.ipor.ticketsystem.model.dynamic.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class RolUsuario {
    public RolUsuario(RolUsuarioRecord rolUsuarioRecord) {
        this.nombre = rolUsuarioRecord.nombre();
        this.PUEDE_CREAR_TICKET = rolUsuarioRecord.PUEDE_CREAR_TICKET();
        this.PUEDE_EDITAR_TICKET = rolUsuarioRecord.PUEDE_EDITAR_TICKET();
        this.PUEDE_CANCELAR_TICKET = rolUsuarioRecord.PUEDE_CANCELAR_TICKET();
        this.PUEDE_VER_SUS_TICKET = rolUsuarioRecord.PUEDE_VER_SUS_TICKET();
        this.PUEDE_VER_HISTORIAL_TICKET = rolUsuarioRecord.PUEDE_VER_HISTORIAL_TICKET();
        this.PUEDE_VER_LISTA_TICKET = rolUsuarioRecord.PUEDE_VER_LISTA_TICKET();
        this.PUEDE_ATENDER_TICKET = rolUsuarioRecord.PUEDE_ATENDER_TICKET();
        this.PUEDE_CERRAR_TICKET = rolUsuarioRecord.PUEDE_CERRAR_TICKET();
        this.PUEDE_MODIFICAR_TABLAS = rolUsuarioRecord.PUEDE_MODIFICAR_TABLAS();
        this.PUEDE_EXPORTAR_DATOS = rolUsuarioRecord.PUEDE_EXPORTAR_DATOS();
        this.PUEDE_VER_ESTADISTICAS = rolUsuarioRecord.PUEDE_VER_ESTADISTICAS();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @OneToMany(mappedBy = "rolUsuario")
    private Set<Usuario> listaUsuarios;

    private Boolean PUEDE_CREAR_TICKET;
    private Boolean PUEDE_EDITAR_TICKET;
    private Boolean PUEDE_CANCELAR_TICKET;
    private Boolean PUEDE_VER_SUS_TICKET;
    private Boolean PUEDE_VER_HISTORIAL_TICKET;
    private Boolean PUEDE_VER_LISTA_TICKET;
    private Boolean PUEDE_ATENDER_TICKET;
    private Boolean PUEDE_CERRAR_TICKET;
    private Boolean PUEDE_MODIFICAR_TABLAS;
    private Boolean PUEDE_EXPORTAR_DATOS;
    private Boolean PUEDE_VER_ESTADISTICAS;

}
