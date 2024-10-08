package com.ipor.ticketsystem.model.dynamic;
import com.ipor.ticketsystem.model.fixed.ClasificacionServicio;
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
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private LocalTime hora;

    @Column(columnDefinition = "VARCHAR(MAX)")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name= "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name= "id_clasificacion_servicio")
    private ClasificacionServicio clasificacionServicio;

    @OneToOne
    @JoinColumn(name = "id_ticket")
    private Ticket ticket;



    @PrePersist
    public void prePersist() {
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
    }
}
