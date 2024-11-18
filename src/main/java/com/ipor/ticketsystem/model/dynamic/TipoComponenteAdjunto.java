package com.ipor.ticketsystem.model.dynamic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CompraComponenteAdjunto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer cantidad;
    @Column(nullable = false)
    private boolean aprobado;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_ticket", nullable = false)
    private Ticket ticket;


}
