package com.ipor.ticketsystem.usuario.login;

import com.ipor.ticketsystem.core.security.ConstantesSeguridad;
import com.ipor.ticketsystem.core.security.JwtAuthenticationFilter;
import com.ipor.ticketsystem.core.security.JwtTokenProvider;
import com.ipor.ticketsystem.usuario.Usuario;
import com.ipor.ticketsystem.usuario.UsuarioService;
import com.ipor.ticketsystem.usuario.integracionSpringERP.SpringUserService;
import com.ipor.ticketsystem.usuario.integracionSpringERP.UsuarioSpringDTO;
import com.ipor.ticketsystem.usuario.rol.RolUsuario;
import com.ipor.ticketsystem.usuario.rol.RolUsuarioRepository;
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

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class LoginService {
    @Autowired
    SpringUserService springUserService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    RolUsuarioRepository rolUsuarioRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    public ResponseEntity<Void> logearUsuarioAlSistema(String username, String password, HttpServletResponse response) throws IOException {
        try {
            Boolean existeEnSpring = springUserService.obtenerValidacionLoginSpring(username, password);
            Usuario usuarioTicket;
            if (existeEnSpring == null) {
                if (usuarioService.getUsuarioPorUsername(username).isPresent()) {
                    usuarioTicket = usuarioService.getUsuarioPorUsername(username).get();
                } else {
                    username = URLEncoder.encode(username, StandardCharsets.UTF_8);
                    response.sendRedirect("/login?error=unknown&username=" + username);
                    System.out.println("Error 1");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            } else {

                Boolean existeEnTickets = usuarioService.existeUsuarioPorUsername(username);
                UsuarioSpringDTO usuarioSpringDTO = springUserService.obtenerUsuarioSpring(username);

                if (existeEnSpring && existeEnTickets) {
                    //reemplazar datos de la bd tickets con datos de la bd
                    usuarioTicket = usuarioService.getUsuarioPorUsername(username).get();
                    usuarioTicket.setChangedPass(true);
                    usuarioTicket.setIsSpringUser(Boolean.TRUE);
                    usuarioTicket.setNombre(usuarioSpringDTO.getNombre().toUpperCase());
                    usuarioTicket.asignarYEncriptarPassword(password);
                    usuarioService.guardarUsuario(usuarioTicket);
                } else if (existeEnSpring && !existeEnTickets) {
                    usuarioTicket = new Usuario();
                    usuarioTicket.setIsActive(true);
                    usuarioTicket.setRolUsuario(rolUsuarioRepository.findById(1l).get());
                    usuarioTicket.setIsSpringUser(true);
                    usuarioTicket.setChangedPass(true);
                    usuarioTicket.setNombre(usuarioSpringDTO.getNombre().toUpperCase());
                    usuarioTicket.setUsername(usuarioSpringDTO.getUsuario().toUpperCase());
                    usuarioTicket.asignarYEncriptarPassword(password);
                    usuarioService.guardarUsuario(usuarioTicket);
                } else {
                    if (usuarioService.getUsuarioPorUsername(username).isPresent()) {
                        usuarioTicket = usuarioService.getUsuarioPorUsername(username).get();
                    } else {
                        username = URLEncoder.encode(username, StandardCharsets.UTF_8);
                        response.sendRedirect("/login?error=badCredentials&username=" + username);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                    }
                }
            }
            if (!usuarioTicket.getIsActive()) {
                username = URLEncoder.encode(username, StandardCharsets.UTF_8);
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
            //System.out.println(e.getMessage());
            username = URLEncoder.encode(username, StandardCharsets.UTF_8);
            response.sendRedirect("/login?error=badCredentials&username=" + username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            //response.sendRedirect("/login?error=unknown&username=" + username);
            System.out.println("Error 2");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
