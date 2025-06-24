package com.ipor.ticketsystem.usuario.integracionSpringERP;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SpringUserService {
    @Autowired
    RestTemplate restTemplate;

    public Boolean obtenerValidacionLoginSpring(String username, String password) {
        String url = "http://localhost:9000/api/usuarios/validar?username=" + username+ "&password=" + password;
        try {
            return restTemplate.getForObject(url, Boolean.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public UsuarioSpringDTO obtenerUsuarioSpring(String nombreUsuario) {
        String url = "http://localhost:9000/api/usuarios/" + nombreUsuario;
        return restTemplate.getForObject(url, UsuarioSpringDTO.class);
    }

}
