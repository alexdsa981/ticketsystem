package com.ipor.ticketsystem.model.fixed;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(nullable = false)
    private String nombre;

    @JsonIgnore
    @OneToMany(mappedBy = "rolUsuario")
    private Set<Usuario> listaUsuarios;

}