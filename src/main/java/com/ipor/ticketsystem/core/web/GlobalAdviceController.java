package com.ipor.ticketsystem.core.web;

import com.ipor.ticketsystem.usuario.Usuario;
import com.ipor.ticketsystem.ticket.comun.HorarioAtencionSoporte;
import com.ipor.ticketsystem.ticket.service.ClasificadoresService;
import com.ipor.ticketsystem.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalAdviceController {
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ClasificadoresService clasificadoresService;



    @ModelAttribute
    public void datosHeader(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getPrincipal().equals("anonymousUser")) {
            Usuario usuario = usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado());
            model.addAttribute("idUsuarioLogeado", usuario.getId());
            model.addAttribute("idRol", usuario.getRolUsuario().getId());
            model.addAttribute("nombreUsuario", usuario.getNombre());
            // Obtener la hora actual del servidor
            LocalDateTime now = LocalDateTime.now(); // Hora actual
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // Formato de hora
            String formattedTime = now.format(formatter); // Hora formateada
            model.addAttribute("horaActual", formattedTime);
            HorarioAtencionSoporte horarioAtencionSoporte = clasificadoresService.getLastHorarioAtencionSoporte();
            model.addAttribute("horarioAtencionSoporte", horarioAtencionSoporte);

        }
    }

}
