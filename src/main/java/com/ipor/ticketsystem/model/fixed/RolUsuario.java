package com.ipor.ticketsystem.model.fixed;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre_rol;

    @OneToMany(mappedBy = "rolUsuario")
    private Set<Usuario> listaUsuarios;

    //USUARIOS
    private Boolean PUEDE_CREAR_TICKET;
    private Boolean PUEDE_EDITAR_TICKET;
    private Boolean PUEDE_CANCELAR_TICKET;
    private Boolean PUEDE_VER_SUS_TICKET;

    //SOPORTE
    private Boolean PUEDE_VER_HISTORIAL_TICKET;
    private Boolean PUEDE_VER_LISTA_TICKET;
    private Boolean PUEDE_ATENDER_TICKET;
    private Boolean PUEDE_CERRAR_TICKET;

    //ADMIN
    private Boolean PUEDE_MODIFICAR_TABLAS;
    private Boolean PUEDE_EXPORTAR_DATOS;
    private Boolean PUEDE_VER_ESTADISTICAS;

}
