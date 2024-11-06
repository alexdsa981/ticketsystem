package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dynamic.Recepcion;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.model.fixed.FaseTicket;
import com.ipor.ticketsystem.model.fixed.RolUsuario;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import com.ipor.ticketsystem.repository.dynamic.UsuarioRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionUrgenciaRepository;
import com.ipor.ticketsystem.repository.fixed.FaseTicketRepository;
import com.ipor.ticketsystem.repository.fixed.RolUsuarioRepository;
import com.ipor.ticketsystem.security.JwtAuthenticationFilter;
import com.ipor.ticketsystem.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    FaseTicketRepository faseTicketRepository;

    public Long RetornarIDdeUsuarioLogeado(){

        String token = jwtAuthenticationFilter.tokenActual;
        String username = jwtTokenProvider.obtenerUsernameDeJWT(token);
        Usuario usuario = usuarioRepository.findByUsername(username).get();
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

            List<Ticket> listaTicketsFase1 = ticketRepository.findByUsuarioIdAndFaseTicketId(usuario.getId(), 1L);
            List<Ticket> listaTicketsFase2 = ticketRepository.findByUsuarioIdAndFaseTicketId(usuario.getId(), 2L);

            List<Ticket> listaTicketsADesactivar = new ArrayList<>();
            listaTicketsADesactivar.addAll(listaTicketsFase1);
            listaTicketsADesactivar.addAll(listaTicketsFase2);

            FaseTicket faseDesactivado = faseTicketRepository.findByNombre("Desestimado");


            for (Ticket ticket : listaTicketsADesactivar) {
                ticket.setFaseTicket(faseDesactivado);
            }
            ticketRepository.saveAll(listaTicketsADesactivar);

        }
    }
    public void activarUsuario(Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setIsActive(true); // Cambia el estado activo a 'true'
            usuarioRepository.save(usuario); // Guarda el usuario actualizado
        }
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
