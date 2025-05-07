package com.ipor.ticketsystem.model.dynamic;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate fecha;
    @Column(nullable = false)
    private LocalTime hora;
    @Column(nullable = true)
    private Boolean leido;
    @Column(nullable = true)
    private Boolean abierto;
    @Column(nullable = true)
    private String url;
    @Column(nullable = false, columnDefinition = "VARCHAR(MAX)")
    private String mensaje;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "id_ticket", nullable = false)
    private Ticket ticket;


    // Método para establecer fecha y hora actuales antes de persistir
    @PrePersist
    public void prePersist() {
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
    }
}
