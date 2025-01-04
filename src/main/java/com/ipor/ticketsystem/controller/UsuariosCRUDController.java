package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.dynamic.Desestimacion;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.model.fixed.RolUsuario;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import com.ipor.ticketsystem.service.AtencionService;
import com.ipor.ticketsystem.service.ClasificadoresService;
import com.ipor.ticketsystem.service.TicketService;
import com.ipor.ticketsystem.service.UsuarioService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import com.ipor.ticketsystem.model.dynamic.Usuario;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/app/usuarios")
public class UsuariosCRUDController {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private AtencionService atencionService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ClasificadoresService clasificadoresService;

    // Mostrar la lista de usuarios
    @GetMapping
    public Model listarUsuarios(Model model) {
        List<Usuario> listaUsuarios = usuarioService.getListaUsuarios();
        model.addAttribute("ListaUsuarios", listaUsuarios);
        return model;
    }
    public Model listarRoles(Model model){
        List<RolUsuario> listaRoles = usuarioService.getListaRoles();
        model.addAttribute("ListaRoles", listaRoles);
        return model;
    }

    @PostMapping("/nuevo")
    public ResponseEntity<String> crearUsuario(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("nombre") String nombre,
            @RequestParam("rolUsuario") Long rolId,
            HttpServletResponse response) throws IOException {

        try {
            // Lógica para crear el usuario
            Usuario usuario = new Usuario();
            usuario.setRolUsuario(usuarioService.getRolPorId(rolId));
            usuario.setNombre(nombre);
            usuario.setPassword(password);
            usuario.setUsername(username);
            usuario.setIsActive(Boolean.TRUE);
            usuario.setChangedPass(Boolean.FALSE);

            // Guardar usuario
            usuarioService.guardarUsuario(usuario);

            // Redirigir a la página de usuarios con mensaje de éxito
            response.sendRedirect("/admin/Usuarios?successful=newUser");
            return ResponseEntity.ok("Usuario creado correctamente");

        } catch (DataIntegrityViolationException e) {
            // Error por duplicado (usuario ya existe)
            response.sendRedirect("/admin/Usuarios?error=duplicated-user");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: El usuario ya existe.");

        } catch (Exception e) {
            // Error general
            response.sendRedirect("/admin/Usuarios?error=general-user");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el usuario.");
        }
    }



    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id,
                                    @RequestParam("username") String username,
                                    @RequestParam("password") String password,
                                    @RequestParam("nombre") String nombre,
                                    @RequestParam("rol") Long rolId) {
        try {
            // Lógica para actualizar el usuario
            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            if (password != null && !password.isEmpty()) {
                usuario.setPassword(password);
            }
            usuario.setNombre(nombre);
            usuario.setRolUsuario(usuarioService.getRolPorId(rolId));
            usuario.setIsActive(Boolean.TRUE);

            // Actualizar el usuario en la base de datos
            usuarioService.actualizarUsuario(id, usuario);

            // Redirigir a la página de usuarios con mensaje de éxito
            return "redirect:/admin/Usuarios?successfull=updateUser";

        } catch (DataIntegrityViolationException e) {
            // Error por duplicado (usuario ya existe)
            return "redirect:/admin/Usuarios?error=duplicated-user";

        } catch (Exception e) {
            // Error general
            return "redirect:/admin/Usuarios?error=general-user";
        }
    }


    // desactivar un usuario
    @GetMapping("/desactivar/{id}")
    public String desactivarUsuario(@PathVariable Long id) {
        if (!Objects.equals(usuarioService.getIDdeUsuarioLogeado(), id)) {
            usuarioService.desactivarUsuario(id);

            List<Ticket>listaTicketsUsuarioFase1 = ticketRepository.findByUsuarioIdAndFaseTicketId(id, 1L);

            for (Ticket ticket : listaTicketsUsuarioFase1){
                System.out.println("creados: "+ticket.getId());
                Desestimacion ticketDesestimado = new Desestimacion();
                //CAMBIAR A AL NUMERO QUE DIGA USUARIO DESACTIVADO
                ticketDesestimado.setClasificacionDesestimacion(clasificadoresService.getClasificacionDesestimacionPorId(1L));
                ticketDesestimado.setUsuario(usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado()));
                ticketDesestimado.setTicket(ticket);
                ticketDesestimado.setDescripcion("Desestimado por desactivación de usuario: " + ticket.getUsuario().getNombre());
                ticketDesestimado.setFecha(ticketDesestimado.getFecha());
                ticketDesestimado.setHora(ticketDesestimado.getHora());
                atencionService.saveDesestimacion(ticketDesestimado);
                //CAMBIAR A NUMERO DEL ID FASE DESESTIMADO
                ticket.setFaseTicket(ticketService.getFaseTicketPorID(4L));
                ticketRepository.save(ticket);
            }

            List<Ticket>listaTicketsUsuarioFase2 = ticketRepository.findByUsuarioIdAndFaseTicketId(id, 2L);
            for (Ticket ticket : listaTicketsUsuarioFase2){
                System.out.println("recepcionados:" + ticket.getId());
                Desestimacion ticketDesestimado = new Desestimacion();
                //CAMBIAR A AL NUMERO QUE DIGA USUARIO DESACTIVADO
                ticketDesestimado.setClasificacionDesestimacion(clasificadoresService.getClasificacionDesestimacionPorId(2L));
                ticketDesestimado.setUsuario(usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado()));
                ticketDesestimado.setTicket(ticket);
                ticketDesestimado.setDescripcion("Desestimado por desactivación de usuario: " + ticket.getUsuario().getNombre());
                ticketDesestimado.setFecha(ticketDesestimado.getFecha());
                ticketDesestimado.setHora(ticketDesestimado.getHora());
                atencionService.saveDesestimacion(ticketDesestimado);
                //CAMBIAR A NUMERO DEL ID FASE DESESTIMADO
                ticket.setFaseTicket(ticketService.getFaseTicketPorID(4L));
                ticketRepository.save(ticket);
                //ELIMINAR REGISTRO DE RECEPCION
                atencionService.deleteRecepcion(atencionService.findRecepcionByTicketID(ticket.getId()));

            }

            
        }
        return "redirect:/admin/Usuarios";
    }
    // activar un usuario
    @GetMapping("/activar/{id}")
    public String activarUsuario(@PathVariable Long id) {
        if (!Objects.equals(usuarioService.getIDdeUsuarioLogeado(), id)) {
            usuarioService.activarUsuario(id); // Llama al método para activar
        }
        return "redirect:/admin/Usuarios";
    }


    @GetMapping("/rol")
    public ResponseEntity<String> obtenerRolUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Usuario usuario = usuarioService.getUsuarioPorUsername(userDetails.getUsername());

            if (usuario != null && usuario.getRolUsuario().getId() == 2L) {
                return ResponseEntity.ok("soporte");
            }
        }
        return ResponseEntity.ok("otro");
    }
    @GetMapping("/id")
    public ResponseEntity<Long> obtenerIdUsuarioLogeado() {
        Long id = usuarioService.getIDdeUsuarioLogeado();
        return ResponseEntity.ok(id);
    }


}
