package com.ipor.ticketsystem.model.fixed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ipor.ticketsystem.model.dynamic.Recepcion;
import com.ipor.ticketsystem.model.dynamic.Ticket;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @JsonIgnore
    @OneToMany(mappedBy = "clasificacionUrgencia")
    private List<Recepcion> listaRecepcion;
}
