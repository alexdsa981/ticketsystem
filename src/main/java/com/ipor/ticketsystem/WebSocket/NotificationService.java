package com.ipor.ticketsystem.WebSocket;

import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.service.UsuarioService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final UsuarioService usuarioService;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(UsuarioService usuarioService, SimpMessagingTemplate messagingTemplate) {
        this.usuarioService = usuarioService;
        this.messagingTemplate = messagingTemplate;
    }

    public void enviarNotificacion(String message) {
        messagingTemplate.convertAndSend("/topic/notificaciones/soporte", message);
    }
}
