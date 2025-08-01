package com.ipor.ticketsystem.ticket;
import com.ipor.ticketsystem.usuario.Usuario;
import com.ipor.ticketsystem.ticket.adjuntos.ArchivoAdjuntoDesestimacion;
import com.ipor.ticketsystem.ticket.clasificadores.model.ClasificacionDesestimacion;
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
public class Desestimacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate fecha;
    @Column(nullable = false)
    private LocalTime hora;
    private LocalDateTime fechaHora;

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

    @OneToMany(mappedBy = "desestimacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArchivoAdjuntoDesestimacion> listaArchivosAdjuntos = new ArrayList<>();



    @PrePersist
    public void prePersist() {
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
        this.fechaHora = LocalDateTime.now();

    }
}
