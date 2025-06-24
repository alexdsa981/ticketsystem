package com.ipor.ticketsystem.ticket.clasificadores.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ipor.ticketsystem.ticket.Atencion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TipoIncidencia {
    public TipoIncidencia(String nombre, SubCategoriaIncidencia subcategoria, Boolean isActive) {
        this.subCategoriaIncidencia = subcategoria;
        this.isActive = isActive;
        this.nombre = nombre;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;


    @Column(nullable = false)
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "id_subcategoria_incidencia")
    private SubCategoriaIncidencia subCategoriaIncidencia;

    @JsonIgnore
    @OneToMany(mappedBy = "tipoIncidencia")
    private List<Atencion> listaAtencion;


}
