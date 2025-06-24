package com.ipor.ticketsystem.ticket.clasificadores.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ipor.ticketsystem.ticket.Ticket;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class FaseTicket {

    public FaseTicket(String nombre){
        this.nombre = nombre;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;

    @JsonIgnore
    @OneToMany(mappedBy = "faseTicket")
    private List<Ticket> listaTickets;
}
