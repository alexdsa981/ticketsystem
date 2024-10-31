package com.ipor.ticketsystem.model.fixed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ipor.ticketsystem.model.dynamic.Servicio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ClasificacionServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Boolean isActive;
    @JsonIgnore
    @OneToMany(mappedBy = "clasificacionServicio")
    private Set<Servicio> listaServicios;
}
