package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.model.fixed.RolUsuario;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import com.ipor.ticketsystem.repository.dynamic.UsuarioRepository;
import com.ipor.ticketsystem.repository.fixed.FaseTicketRepository;
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

    public Long getIDdeUsuarioLogeado(){

        String token = jwtAuthenticationFilter.tokenActual;
        String username = jwtTokenProvider.obtenerUsernameDeJWT(token);
        Usuario usuario = usuarioRepository.findByUsername(username).get();
        return usuario.getId();
    }
    public Usuario getUsuarioPorId(Long id){
        return usuarioRepository.findById(id).get();
    }

    // Crear un nuevo usuario
    public Usuario guardarUsuario(Usuario usuario) {
        usuario.encriptarPassword(usuario.getPassword());
        return usuarioRepository.save(usuario);
    }

    // Obtener todos los usuarios
    public List<Usuario> getListaUsuarios() {
        return usuarioRepository.findAll();
    }
    //activos
    public List<Usuario> getListaUsuariosActivos() {
        return usuarioRepository.findByIsActiveTrue();
    }
    //desactivados
    public List<Usuario> getListaUsuariosDesactivados() {
        return usuarioRepository.findByIsActiveFalse();
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
                usuario.setChangedPass(Boolean.FALSE);
            }
            return usuarioRepository.save(usuario);
        }
        return null; // O puedes lanzar una excepción personalizada si no existe
    }

//    // Eliminar un usuario por ID
//    public boolean eliminarUsuario(Long id) {
//        if (usuarioRepository.existsById(id)) {
//            usuarioRepository.deleteById(id);
//            return true;
//        }
//        return false;
//    }

    public void desactivarUsuario(Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setIsActive(false);
            usuarioRepository.save(usuario);
        }
    }
    public void activarUsuario(Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setIsActive(true);
            usuarioRepository.save(usuario);
        }
    }
    //retorna lista de usuarios por rol
    public List<Usuario> ListaUsuariosPorRol(Long idRolUsuario){
        return usuarioRepository.findByRolUsuarioId(idRolUsuario);
    }
//    //retorna todos los roles:
    public List<RolUsuario> getListaRoles(){
        return rolUsuarioRepository.findAll();
    }
    public RolUsuario getRolPorId(Long id){
        return rolUsuarioRepository.findById(id).get();
    }
    public Usuario getUsuarioPorUsername(String username){
        return usuarioRepository.findByUsername(username).get();
    }

    public Boolean existeUsuarioPorUsername(String username){
        Optional<Usuario> usuarioOpt =  usuarioRepository.findByUsername(username);
        if (usuarioOpt.isPresent()){
            System.out.println("Existe en bd Tickets");
            return true;
        }else{
            System.out.println("No existe en bd tickets");
            return false;
        }

    }
}
