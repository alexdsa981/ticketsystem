package com.ipor.ticketsystem.ticket;
import com.ipor.ticketsystem.usuario.Usuario;
import com.ipor.ticketsystem.ticket.adjuntos.ArchivoAdjuntoAtencion;
import com.ipor.ticketsystem.ticket.clasificadores.model.AreaAtencion;
import com.ipor.ticketsystem.ticket.clasificadores.model.TipoIncidencia;
import com.ipor.ticketsystem.ticket.clasificadores.model.ClasificacionAtencion;
import com.ipor.ticketsystem.ticket.clasificadores.model.ClasificacionUrgencia;
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
public class Atencion {
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

    @OneToMany(mappedBy = "atencion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArchivoAdjuntoAtencion> listaArchivosAdjuntos = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name= "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name= "id_clasificacion_atencion", nullable = false)
    private ClasificacionAtencion clasificacionAtencion;

    @ManyToOne
    @JoinColumn(name = "id_tipo_incidencia", nullable = true)
    private TipoIncidencia tipoIncidencia;

    @ManyToOne
    @JoinColumn(name = "id_area_atencion", nullable = true)
    private AreaAtencion areaAtencion;

    @ManyToOne
    @JoinColumn(name = "id_clasificacion_urgencia", nullable = false)
    private ClasificacionUrgencia clasificacionUrgencia;

    @OneToOne
    @JoinColumn(name = "id_ticket", nullable = false)
    private Ticket ticket;



    @PrePersist
    public void prePersist() {
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
        this.fechaHora = LocalDateTime.now();

    }
}
