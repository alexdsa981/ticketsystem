package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalAdviceController {
@Autowired
UsuarioService usuarioService;

    @ModelAttribute
    public void datosHeader(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getPrincipal().equals("anonymousUser")) {
            Usuario usuario = new Usuario();
                usuario = usuarioService.RetornarUsuarioPorId(usuarioService.RetornarIDdeUsuarioLogeado());
            model.addAttribute("nombreUsuario", usuario.getNombre());
            // Obtener la hora actual del servidor
            LocalDateTime now = LocalDateTime.now(); // Hora actual
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // Formato de hora
            String formattedTime = now.format(formatter); // Hora formateada
            model.addAttribute("horaActual", formattedTime);

        }
    }
}