package com.ipor.ticketsystem.model.dynamic;
import com.ipor.ticketsystem.model.fixed.ClasificacionDesestimacion;
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
public class Desestimacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate fecha;
    @Column(nullable = false)
    private LocalTime hora;

    @Column(nullable = false, columnDefinition = "VARCHAR(MAX)")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name= "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name= "id_clasificacion_desestimacion", nullable = false)
    private ClasificacionDesestimacion clasificacionDesestimacion;

    @OneToOne
    @JoinColumn(name = "id_ticket", nullable = false)
    private Ticket ticket;

    @PrePersist
    public void prePersist() {
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
    }
}
