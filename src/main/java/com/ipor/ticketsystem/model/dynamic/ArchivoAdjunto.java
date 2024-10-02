package com.ipor.ticketsystem.model.dynamic;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ArchivoAdjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Lob
    private byte[] archivo;

    private String tipoContenido;
    private Double pesoContenido;

    @ManyToOne
    @JoinColumn(name = "id_ticket", nullable = false)
    private Ticket ticket;
}
