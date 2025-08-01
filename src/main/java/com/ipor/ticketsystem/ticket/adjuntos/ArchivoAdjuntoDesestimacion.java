package com.ipor.ticketsystem.ticket.adjuntos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ipor.ticketsystem.ticket.Desestimacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ArchivoAdjuntoDesestimacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Lob
    private byte[] archivo;

    private String tipoContenido;
    private Double pesoContenido;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_desestimacion", nullable = false)
    private Desestimacion desestimacion;

    // Método para formatear el peso del archivo en MB
    public String getPesoEnMegabytes() {
        if(this.pesoContenido != null){
            double pesoEnKB = this.pesoContenido; // pesoContenido está en KB
            double pesoEnMB = pesoEnKB / 1024; // Convertir a MB

            // Formatear a 2 decimales
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(pesoEnMB) + " MB";
        }else{
            return "Error";
        }

    }

}
