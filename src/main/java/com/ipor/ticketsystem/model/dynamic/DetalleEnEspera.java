package com.ipor.ticketsystem.model.dynamic;

import com.ipor.ticketsystem.model.fixed.ClasificacionEspera;
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
public class DetalleEnEspera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_ticket", nullable = false)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_clasificacion_espera", nullable = false)
    private ClasificacionEspera clasificacionEspera;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private LocalDate fechaInicio;
    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;
    @Column(nullable = false)
    private LocalTime horaFin;
}
