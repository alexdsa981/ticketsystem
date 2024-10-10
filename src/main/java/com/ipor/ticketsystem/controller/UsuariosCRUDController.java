package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.fixed.RolUsuario;
import com.ipor.ticketsystem.service.UsuarioService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import com.ipor.ticketsystem.model.dynamic.Usuario;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuariosCRUDController {

    @Autowired
    private UsuarioService usuarioService;

    // Mostrar la lista de usuarios
    @GetMapping
    public Model listarUsuarios(Model model) {
        List<Usuario> listaUsuarios = usuarioService.obtenerTodosLosUsuarios();
        model.addAttribute("ListaUsuarios", listaUsuarios);
        return model;
    }
    public Model listarRoles(Model model){
        List<RolUsuario> listaRoles = usuarioService.retornaTodosLosRoles();
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
        usuario.setRolUsuario(usuarioService.retornarRolPorId(rolId));
        usuario.setNombre(nombre);
        usuario.setPassword(password);
        usuario.setUsername(username);
        usuarioService.guardarUsuario(usuario);
        response.sendRedirect("/Usuarios");
        return ResponseEntity.ok("Usuario creado correctamente");
    }


    // Guardar un nuevo usuario
    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        usuarioService.guardarUsuario(usuario);
        return "redirect:/usuarios";
    }

    // Mostrar el formulario para editar un usuario existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.RetornarUsuarioPorId(id);
        model.addAttribute("usuario", usuario);
        return "usuarios/formulario"; // Nombre de la vista de Thymeleaf para editar un usuario
    }
    // Actualizar un usuario existente
    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id, @ModelAttribute("usuario") Usuario usuario) {
        usuarioService.actualizarUsuario(id, usuario);
        return "redirect:/usuarios";
    }

    // Eliminar un usuario
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/usuarios";
    }
}
