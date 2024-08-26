package com.ipor.ticketsystem.model.dynamic;

import com.ipor.ticketsystem.model.fixed.RolUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_tipoUsuario")
    private RolUsuario rolUsuario;

}
