package com.ipor.ticketsystem.model.dynamic;

import com.ipor.ticketsystem.model.fixed.ClasificacionServicio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
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

}
