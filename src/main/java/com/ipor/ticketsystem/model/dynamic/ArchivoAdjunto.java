package com.ipor.ticketsystem.model.dynamic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.Base64;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ArchivoAdjunto {

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
    @JoinColumn(name = "id_ticket", nullable = false)
    private Ticket ticket;

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
