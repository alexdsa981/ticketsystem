package com.ipor.ticketsystem.ticket.clasificadores.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ipor.ticketsystem.ticket.Atencion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class AreaAtencion {
    public AreaAtencion(String nombre, Sede sede, Boolean isActive) {
        this.nombre = nombre;
        this.isActive = isActive;
        this.sede = sede;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "id_sede")
    private Sede sede;

    @JsonIgnore
    @OneToMany(mappedBy = "areaAtencion")
    private List<Atencion> listaAtencion;
}
