package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.model.fixed.HorarioAtencionSoporte;
import com.ipor.ticketsystem.service.ClasificadoresService;
import com.ipor.ticketsystem.service.UsuarioService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
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
            Usuario usuario = new Usuario();
                usuario = usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado());
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
