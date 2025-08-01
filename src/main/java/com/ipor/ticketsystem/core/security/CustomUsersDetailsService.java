package com.ipor.ticketsystem.core.security;

import com.ipor.ticketsystem.usuario.Usuario;
import com.ipor.ticketsystem.usuario.rol.RolUsuario;
import com.ipor.ticketsystem.usuario.UsuarioRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class CustomUsersDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    private final HttpServletResponse response; // Añadido

    @Autowired
    public CustomUsersDetailsService(UsuarioRepository usuarioRepository, HttpServletResponse response) {
        this.usuarioRepository = usuarioRepository;
        this.response = response; // Inicializado
    }

    //se crea una lista de autoridades por medio de una lista de roles
    public Collection<? extends GrantedAuthority> mapAuthority(Collection<RolUsuario> roles) {
        return roles.stream().map(rol -> new SimpleGrantedAuthority(rol.getNombre())).collect(Collectors.toList());
    }

    //usamos singletonlist para solo alojar un unico rol segun la logica de negocio
    //obtenemos un usuario con todos sus datos por medio de su username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!usuario.getIsActive()) {
            throw new UsernameNotFoundException("User not found"); // Se maneja igual
        }
        Collection<RolUsuario> roles = Collections.singletonList(usuario.getRolUsuario());
        return new User(usuario.getUsername(), usuario.getPassword(), mapAuthority(roles));
    }
}


