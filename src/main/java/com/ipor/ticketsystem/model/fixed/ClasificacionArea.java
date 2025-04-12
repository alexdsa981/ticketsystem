package com.ipor.ticketsystem.model.fixed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ipor.ticketsystem.model.dynamic.Desestimacion;
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
public class ClasificacionArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private Boolean isActive;

    @JsonIgnore
    @OneToMany(mappedBy = "clasificacionArea")
    private List<Ticket> listaTickets;
}
