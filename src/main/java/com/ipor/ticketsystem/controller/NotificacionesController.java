package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.service.NotificacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/notificaciones")
public class NotificacionesController {
    @Autowired
    NotificacionesService notificacionesService;

    @GetMapping()
    public ResponseEntity<String> getNotificaciones() {
        notificacionesService.getNotificacionesDeUsuarioLogeado();
        return ResponseEntity.ok("Notificaciones cargadas");
    }
}
