package com.ipor.ticketsystem.ticket;

import com.ipor.ticketsystem.usuario.Usuario;
import com.ipor.ticketsystem.ticket.adjuntos.ArchivoAdjuntoEspera;
import com.ipor.ticketsystem.ticket.clasificadores.model.ClasificacionEspera;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    private LocalDate fechaFin;

    private LocalTime horaFin;

    @OneToMany(mappedBy = "detalleEnEspera", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArchivoAdjuntoEspera> listaArchivosAdjuntos = new ArrayList<>();

    @Transient
    public String ConvertirFechaConFormato(LocalDate localDate) {
        return localDate.format( DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    @Transient
    public String ConvertirHoraConFormato(LocalTime localTime) {
        return localTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

}
