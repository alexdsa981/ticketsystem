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
public class ClasificacionUrgencia {

    public ClasificacionUrgencia(String nombre, Boolean isActive){
        this.nombre = nombre;
        this.isActive = isActive;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private Boolean isActive;

    @JsonIgnore
    @OneToMany(mappedBy = "clasificacionUrgencia")
    private List<Atencion> listaAtencion;
}
