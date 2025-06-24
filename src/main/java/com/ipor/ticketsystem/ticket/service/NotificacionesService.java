package com.ipor.ticketsystem.ticket.service;

import com.ipor.ticketsystem.usuario.UsuarioService;
import com.ipor.ticketsystem.ticket.comun.Notificacion;
import com.ipor.ticketsystem.ticket.comun.NotificacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Notificacion> getTOP15NotificacionesDeUsuarioLogeado(){
        return notificacionRepository.findTop15ByUsuarioId(usuarioService.getIDdeUsuarioLogeado());
    }
    public void CambiarNotificacionesALeido(){
        List<Notificacion> notificaciones = notificacionRepository.BuscarPorIdUsuario(usuarioService.getIDdeUsuarioLogeado());
        for (Notificacion notificacion : notificaciones){
            notificacion.setLeido(Boolean.TRUE);
            notificacionRepository.save(notificacion);
        }
    }
    public void CambiarNotificacionPorAbierto(Long id){
        Notificacion notificacion = notificacionRepository.findById(id).get();
        notificacion.setAbierto(Boolean.TRUE);
        notificacionRepository.save(notificacion);
    }

}
