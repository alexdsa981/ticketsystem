package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dto.UsuarioSpringDTO;
import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.model.fixed.RolUsuario;
import com.ipor.ticketsystem.repository.dynamic.UsuarioRepository;
import com.ipor.ticketsystem.repository.fixed.RolUsuarioRepository;
import com.ipor.ticketsystem.security.ConstantesSeguridad;
import com.ipor.ticketsystem.security.JwtAuthenticationFilter;
import com.ipor.ticketsystem.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.io.IOException;
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
    RestTemplate restTemplate;
    @Autowired
    private AuthenticationManager authenticationManager;

    public Long getIDdeUsuarioLogeado() {

        String token = jwtAuthenticationFilter.tokenActual;
        String username = jwtTokenProvider.obtenerUsernameDeJWT(token);
        Usuario usuario = usuarioRepository.findByUsername(username).get();
        return usuario.getId();
    }

    public Usuario getUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).get();
    }

    // Crear un nuevo usuario
    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Obtener todos los usuarios
    public List<Usuario> getListaUsuarios() {
        return usuarioRepository.findAllByOrderByIsSpringUserAsc();
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
                usuario.asignarYEncriptarPassword(usuarioActualizado.getPassword());
                usuario.setChangedPass(Boolean.FALSE);
            }
            return usuarioRepository.save(usuario);
        }
        return null; // O puedes lanzar una excepción personalizada si no existe
    }


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
    public List<Usuario> ListaUsuariosPorRol(Long idRolUsuario) {
        return usuarioRepository.findByRolUsuarioId(idRolUsuario);
    }

    //    //retorna todos los roles:
    public List<RolUsuario> getListaRoles() {
        return rolUsuarioRepository.findAll();
    }

    public RolUsuario getRolPorId(Long id) {
        return rolUsuarioRepository.findById(id).get();
    }

    public Optional<Usuario> getUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Boolean existeUsuarioPorUsername(String username) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        if (usuarioOpt.isPresent()) {
            System.out.println("Existe en bd Tickets");
            return true;
        } else {
            System.out.println("No existe en bd tickets");
            return false;
        }

    }

    public ResponseEntity<Void> logearUsuarioAlSistema(String username, String password, HttpServletResponse response) throws IOException {
        try {
            Boolean existeEnSpring = obtenerValidacionLoginSpring(username, password);
            Usuario usuarioTicket;
            if (existeEnSpring == null) {
                if (getUsuarioPorUsername(username).isPresent()) {
                    usuarioTicket = getUsuarioPorUsername(username).get();
                } else {
                    response.sendRedirect("/login?error=unknown&username=" + username);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            } else {

                Boolean existeEnTickets = existeUsuarioPorUsername(username);
                UsuarioSpringDTO usuarioSpringDTO = obtenerUsuarioSpring(username);

                if (existeEnSpring && existeEnTickets) {
                    //reemplazar datos de la bd tickets con datos de la bd
                    usuarioTicket = getUsuarioPorUsername(username).get();
                    usuarioTicket.setChangedPass(true);
                    usuarioTicket.setIsSpringUser(Boolean.TRUE);
                    usuarioTicket.setNombre(usuarioSpringDTO.getNombre().toUpperCase());
                    usuarioTicket.asignarYEncriptarPassword(password);
                    guardarUsuario(usuarioTicket);
                } else if (existeEnSpring && !existeEnTickets) {
                    usuarioTicket = new Usuario();
                    usuarioTicket.setIsActive(true);
                    usuarioTicket.setRolUsuario(rolUsuarioRepository.findById(1l).get());
                    usuarioTicket.setIsSpringUser(true);
                    usuarioTicket.setChangedPass(true);
                    usuarioTicket.setNombre(usuarioSpringDTO.getNombre().toUpperCase());
                    usuarioTicket.setUsername(usuarioSpringDTO.getUsuario().toUpperCase());
                    usuarioTicket.asignarYEncriptarPassword(password);
                    guardarUsuario(usuarioTicket);
                } else {
                    if (getUsuarioPorUsername(username).isPresent()) {
                        usuarioTicket = getUsuarioPorUsername(username).get();
                    } else {
                        response.sendRedirect("/login?error=badCredentials&username=" + username);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                    }
                }
            }
            if (!usuarioTicket.getIsActive()) {
                response.sendRedirect("/login?error=inactive&username=" + username);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generarToken(authentication);
            Cookie jwtCookie = new Cookie("JWT", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setMaxAge((int) (ConstantesSeguridad.JWT_EXPIRATION_TOKEN) / 1000);
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);

            if (!usuarioTicket.getChangedPass()) {
                response.sendRedirect("/inicio?changePassword");
                return ResponseEntity.ok().build();
            }
            //LOGIN EXITOSO
            response.sendRedirect("/inicio");
            return ResponseEntity.ok().build();
        } catch (BadCredentialsException e) {
            System.out.println(e.getMessage());
            response.sendRedirect("/login?error=badCredentials&username=" + username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            response.sendRedirect("/login?error=unknown&username=" + username);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public Boolean obtenerValidacionLoginSpring(String username, String password) {
        String url = "http://localhost:9000/api/usuarios/validar?username=" + username + "&password=" + password;
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
