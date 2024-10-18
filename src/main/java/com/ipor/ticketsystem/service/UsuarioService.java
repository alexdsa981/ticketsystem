package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.model.fixed.RolUsuario;
import com.ipor.ticketsystem.repository.dynamic.UsuarioRepository;
import com.ipor.ticketsystem.repository.fixed.RolUsuarioRepository;
import com.ipor.ticketsystem.security.JwtAuthenticationFilter;
import com.ipor.ticketsystem.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    RolUsuarioRepository rolUsuarioRepository;

    public Long RetornarIDdeUsuarioLogeado(){

        String token = jwtAuthenticationFilter.tokenActual;
        String username = jwtTokenProvider.obtenerUsernameDeJWT(token);
        Usuario usuario = usuarioRepository.findByUsername(username).get();
        System.out.println();
        return usuario.getId();
    }
    public Usuario RetornarUsuarioPorId(Long id){
        return usuarioRepository.findById(id).get();
    }

    // Crear un nuevo usuario
    public Usuario guardarUsuario(Usuario usuario) {
        usuario.encriptarPassword(usuario.getPassword());
        return usuarioRepository.save(usuario);
    }

    // Obtener todos los usuarios
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    // Actualizar un usuario existente
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            usuario.setUsername(usuarioActualizado.getUsername());
            usuario.setNombre(usuarioActualizado.getNombre());
            usuario.setRolUsuario(usuarioActualizado.getRolUsuario());
            // Encriptar la nueva contraseña si es que se actualiza
            if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
                usuario.encriptarPassword(usuarioActualizado.getPassword());
            }
            return usuarioRepository.save(usuario);
        }
        return null; // O puedes lanzar una excepción personalizada si no existe
    }

    // Eliminar un usuario por ID
    public boolean eliminarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false; // O puedes lanzar una excepción personalizada si no existe
    }

    //retorna todos los roles:
    public List<RolUsuario> retornaTodosLosRoles(){
        return rolUsuarioRepository.findAll();
    }
    public RolUsuario retornarRolPorId(Long id){
        return rolUsuarioRepository.findById(id).get();
    }

    public Usuario findByUsername(String username){
        return usuarioRepository.findByUsername(username).get();
    }
}
