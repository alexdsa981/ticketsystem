package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.repository.dynamic.UsuarioRepository;
import com.ipor.ticketsystem.security.JwtAuthenticationFilter;
import com.ipor.ticketsystem.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebService {
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UsuarioRepository usuarioRepository;

    public Long RetornarIDdeUsuario(){
        String token = jwtAuthenticationFilter.tokenActual;
        String username = jwtTokenProvider.obtenerUsernameDeJWT(token);
        Usuario usuario = usuarioRepository.findByUsername(username).get();
        System.out.println();
        return usuario.getId();
    }


}
