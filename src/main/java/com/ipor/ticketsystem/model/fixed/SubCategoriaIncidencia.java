package com.ipor.ticketsystem.model.fixed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class SubCategoriaIncidencia {
    public SubCategoriaIncidencia(String nombre, CategoriaIncidencia categoria, Boolean isActive) {
        this.nombre = nombre;
        this.isActive = isActive;
        this.categoriaIncidencia = categoria;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private Boolean isActive;


    @ManyToOne
    @JoinColumn(name = "id_categoria_incidencia")
    private CategoriaIncidencia categoriaIncidencia;

    @JsonIgnore
    @OneToMany(mappedBy = "subCategoriaIncidencia", cascade = CascadeType.ALL)
    private List<TipoIncidencia> tiposIncidencia;
}
