package com.ipor.ticketsystem.ticket.comun;

import com.ipor.ticketsystem.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class HorarioAtencionSoporte {

    public HorarioAtencionSoporte(LocalTime horaEntrada, LocalTime horaSalida, Usuario usuarioCreacion, LocalDateTime fechaHoraCreacion) {
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaHoraCreacion = fechaHoraCreacion;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalTime horaEntrada;
    @Column(nullable = false)
    private LocalTime horaSalida;


    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuarioCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaHoraCreacion;
}
