package com.ipor.ticketsystem.model.fixed;

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
public class ClasificacionIncidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @OneToMany(mappedBy = "clasificacionIncidencia")
    private List<Ticket> listaTickets;
}
