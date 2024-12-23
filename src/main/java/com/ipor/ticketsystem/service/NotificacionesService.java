package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dynamic.Notificacion;
import com.ipor.ticketsystem.repository.dynamic.NotificacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacionesService {
    @Autowired
    NotificacionesRepository notificacionRepository;
    @Autowired
    UsuarioService usuarioService;
    //guardar notificacion en base de datos
    public void saveNotiicacion(Notificacion notificacion) {
        notificacionRepository.save(notificacion);
    }
    public void getNotificacionesDeUsuarioLogeado(){
        notificacionRepository.BuscarPorIdUsuario(usuarioService.getIDdeUsuarioLogeado());
    }

}
