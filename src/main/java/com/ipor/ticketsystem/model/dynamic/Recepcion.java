package com.ipor.ticketsystem.model.dynamic;

import com.ipor.ticketsystem.model.fixed.ClasificacionUrgencia;
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
public class Recepcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;
    @Column(nullable = false)
    private LocalTime hora;

    @OneToOne
    @JoinColumn(name = "id_ticket", nullable = false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, columnDefinition = "VARCHAR(MAX)")
    private String mensaje;


    // Método para establecer fecha y hora antes de persistir
    @PrePersist
    public void prePersist() {
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
    }
}
