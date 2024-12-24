package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.dto.NotificacionDTO;
import com.ipor.ticketsystem.model.dynamic.Notificacion;
import com.ipor.ticketsystem.service.NotificacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/app/notificaciones")
public class NotificacionesController {
    @Autowired
    NotificacionesService notificacionesService;

    @GetMapping()
    public ResponseEntity<List<NotificacionDTO>> getNotificaciones() {
        List<Notificacion> ListaNotificaciones = notificacionesService.getTOP15NotificacionesDeUsuarioLogeado();
        List<NotificacionDTO> ListaDTO = new ArrayList<>();
        for (Notificacion notificacion : ListaNotificaciones){
            NotificacionDTO notificacionDTO = new NotificacionDTO(notificacion);
            ListaDTO.add(notificacionDTO);
        }
        return ResponseEntity.ok(ListaDTO);
    }

    @PostMapping("/marcar-leidas")
    public ResponseEntity<Void> marcarNotificacionesComoLeidas() {
        notificacionesService.CambiarNotificacionesALeido();
        return ResponseEntity.noContent().build(); // Respuesta HTTP 204 (No Content)
    }

    @PostMapping("/marcar-abierto/{id}")
    public ResponseEntity<Void> marcarNotificacionComoAbiertoPorID(
            @PathVariable Long id
    ) {
        notificacionesService.CambiarNotificacionPorAbierto(id);
        return ResponseEntity.noContent().build();
    }


}
