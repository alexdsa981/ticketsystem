package com.ipor.ticketsystem.usuario;

import com.ipor.ticketsystem.ticket.Desestimacion;
import com.ipor.ticketsystem.ticket.Ticket;
import com.ipor.ticketsystem.ticket.repository.TicketRepository;
import com.ipor.ticketsystem.ticket.service.AtencionService;
import com.ipor.ticketsystem.ticket.service.ClasificadoresService;
import com.ipor.ticketsystem.ticket.service.TicketService;
import com.ipor.ticketsystem.usuario.rol.RolUsuario;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/app/usuarios")
public class UsuariosController {

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
    public Model listarUsuariosActivos(Model model) {
        List<Usuario> listaUsuariosActivos = usuarioService.getListaUsuariosActivos();
        model.addAttribute("ListaUsuariosActivos", listaUsuariosActivos);
        return model;
    }
    public Model listarUsuariosDesactivados(Model model) {
        List<Usuario> listaUsuariosDesactivados = usuarioService.getListaUsuariosDesactivados();
        model.addAttribute("ListaUsuariosDesactivados", listaUsuariosDesactivados);
        return model;
    }
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
            usuario.asignarYEncriptarPassword(password);
            usuario.setNombre(nombre.toUpperCase());
            usuario.setUsername(username.toUpperCase());
            usuario.setIsSpringUser(Boolean.FALSE);
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
            usuario.setUsername(username.toUpperCase());
            if (password != null && !password.isEmpty()) {
                usuario.setPassword(password);
            }
            usuario.setNombre(nombre.toUpperCase());
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
        if (!Objects.equals(usuarioService.getIDdeUsuarioLogeado(), id) && id !=1) {
            usuarioService.desactivarUsuario(id);

            List<Ticket>listaTicketsUsuarioFase1 = ticketRepository.findByUsuarioIdAndFaseTicketId(id, 1L);

            for (Ticket ticket : listaTicketsUsuarioFase1){
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
            Usuario usuario = usuarioService.getUsuarioPorUsername(userDetails.getUsername()).get();

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
