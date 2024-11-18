package com.ipor.ticketsystem.model.dynamic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ipor.ticketsystem.model.fixed.FaseTicket;
import com.ipor.ticketsystem.model.fixed.TipoComponente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TipoComponenteAdjunto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_tipo_componente", nullable = false)
    private TipoComponente tipoComponente;
    @Column(nullable = false)
    private Integer cantidad;
    @Column(nullable = true)
    private Boolean aprobado;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_ticket", nullable = false)
    private Ticket ticket;


}
