package com.ipor.ticketsystem.model.dynamic;

import com.ipor.ticketsystem.model.fixed.ClasificacionIncidencia;
import com.ipor.ticketsystem.model.fixed.FaseTicket;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private LocalTime hora;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_clasificacion_incidencia")
    private ClasificacionIncidencia clasificacionIncidencia;

    @ManyToOne
    @JoinColumn(name = "id_fase_ticket")
    private FaseTicket faseTicket;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArchivoAdjunto> listaArchivosAdjuntos;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Servicio servicio;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Recepcion recepcion;

    @OneToMany(mappedBy = "ticket")
    private List<HistorialTicket> listaHistorial;

}
