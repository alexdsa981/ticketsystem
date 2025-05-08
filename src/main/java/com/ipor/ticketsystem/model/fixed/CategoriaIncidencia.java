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
public class CategoriaIncidencia {
    public CategoriaIncidencia(String nombre, Boolean isActive) {
        this.nombre = nombre;
        this.isActive = isActive;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private Boolean isActive;

    @JsonIgnore
    @OneToMany(mappedBy = "categoriaIncidencia", cascade = CascadeType.ALL)
    private List<SubCategoriaIncidencia> subCategoriaIncidencias;
}
