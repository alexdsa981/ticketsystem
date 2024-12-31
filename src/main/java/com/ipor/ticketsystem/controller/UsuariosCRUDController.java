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

    //crear usuario nuevo
    @PostMapping("/nuevo")
    public ResponseEntity<String> crearUsuario(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("nombre") String nombre,
            @RequestParam("rolUsuario") Long rolId,
            HttpServletResponse response) throws IOException {

        // Lógica para crear el ticket
        Usuario usuario = new Usuario();
        usuario.setRolUsuario(usuarioService.getRolPorId(rolId));
        usuario.setNombre(nombre);
        usuario.setPassword(password);
        usuario.setUsername(username);
        usuario.setIsActive(Boolean.TRUE);
        usuario.setChangedPass(Boolean.FALSE);
        usuarioService.guardarUsuario(usuario);
        response.sendRedirect("/admin/Usuarios");
        return ResponseEntity.ok("Usuario creado correctamente");
    }


    // Actualizar un usuario existente
    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id,
                                    @RequestParam("username") String username,
                                    @RequestParam("password") String password,
                                    @RequestParam("nombre") String nombre,
                                    @RequestParam("rol") Long rolId
                                    ) {
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        if (password != null){
            usuario.setPassword(password);
        }
        usuario.setNombre(nombre);
        usuario.setRolUsuario(usuarioService.getRolPorId(rolId));
        usuario.setIsActive(Boolean.TRUE);
        usuarioService.actualizarUsuario(id, usuario);
        return "redirect:/admin/Usuarios";
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
