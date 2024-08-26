package com.ipor.ticketsystem.model.dynamic;

import com.ipor.ticketsystem.model.fixed.RolUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String nombre;
    private String correo;

    @ManyToOne
    @JoinColumn(name = "id_rol_usuario")
    private RolUsuario rolUsuario;

    @OneToMany(mappedBy = "usuario")
    private Set<Servicio> listaServicios;

    @OneToMany(mappedBy = "usuario")
    private List<Ticket> listaTickets;

    @OneToMany(mappedBy = "usuario")
    private List<Recepcion> listaRecepciones;

    @OneToMany(mappedBy = "usuario")
    private List<HistorialTicket> listaHistorial;


}
