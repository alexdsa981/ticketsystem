package com.ipor.ticketsystem.model.dynamic;

import com.ipor.ticketsystem.model.fixed.RolUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


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

    @ManyToOne
    @JoinColumn(name = "id_rol_usuario")
    private RolUsuario rolUsuario;

    @OneToMany(mappedBy = "usuario")
    private Set<Servicio> listaServicios;

    @OneToMany(mappedBy = "usuario")
    private List<Ticket> listaTickets;

    @OneToMany(mappedBy = "usuario")
    private List<Recepcion> listaRecepciones;

    public void encriptarPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }
    public boolean verificarPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


}