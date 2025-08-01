package com.ipor.ticketsystem.ticket;

import com.ipor.ticketsystem.ticket.comun.HorarioAtencionSoporte;
import com.ipor.ticketsystem.usuario.Usuario;
import com.ipor.ticketsystem.ticket.adjuntos.ArchivoAdjuntoEnvio;
import com.ipor.ticketsystem.ticket.clasificadores.model.FaseTicket;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codigoTicket;

    @Column(nullable = false)
    private LocalDate fecha;
    @Column(nullable = false)
    private LocalTime hora;

    private LocalDateTime fechaHora;


    @Column(nullable = false, columnDefinition = "VARCHAR(MAX)")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_fase_ticket", nullable = false)
    private FaseTicket faseTicket;

    @ManyToOne
    @JoinColumn(name = "id_horario_atencion_soporte")
    private HorarioAtencionSoporte horarioAtencionSoporte;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArchivoAdjuntoEnvio> listaArchivosAdjuntos = new ArrayList<>();

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Atencion atencion;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Recepcion recepcion;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Desestimacion desestimacion;


    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleEnEspera> listaDetalleEsperas = new ArrayList<>();

    // Método para establecer fecha y hora actuales antes de persistir
    @PrePersist
    public void prePersist() {
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
        this.fechaHora = LocalDateTime.now();
    }
}
