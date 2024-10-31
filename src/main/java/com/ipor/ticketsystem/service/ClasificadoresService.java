package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.model.fixed.ClasificacionIncidencia;
import com.ipor.ticketsystem.model.fixed.ClasificacionServicio;
import com.ipor.ticketsystem.model.fixed.ClasificacionUrgencia;
import com.ipor.ticketsystem.repository.fixed.ClasificacionIncidenciaRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionServicioRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionUrgenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClasificadoresService {

    @Autowired
    private ClasificacionIncidenciaRepository clasificacionIncidenciaRepository;

    @Autowired
    private ClasificacionServicioRepository clasificacionServicioRepository;

    @Autowired
    private ClasificacionUrgenciaRepository clasificacionUrgenciaRepository;

    public void saveCIncidencia(ClasificacionIncidencia clasificacionIncidencia){
        clasificacionIncidenciaRepository.save(clasificacionIncidencia);
    }
    public void saveCUrgencia(ClasificacionUrgencia clasificacionUrgencia){
        clasificacionUrgenciaRepository.save(clasificacionUrgencia);
    }
    public void saveCServicio(ClasificacionServicio clasificacionServicio){
        clasificacionServicioRepository.save(clasificacionServicio);
    }

    public List<ClasificacionIncidencia> RetornaListaClasIncidencia(){
        return  clasificacionIncidenciaRepository.findAll();
    }
    public List<ClasificacionUrgencia> RetornaListaClasUrgencia(){
        return  clasificacionUrgenciaRepository.findAll();
    }
    public List<ClasificacionServicio> RetornaListaClasServicio(){
        return  clasificacionServicioRepository.findAll();
    }

    // Actualizar una clasificación de incidencia existente
    public void actualizarIncidencia(Long id, ClasificacionIncidencia incidenciaActualizada) {
        ClasificacionIncidencia incidencia = clasificacionIncidenciaRepository.findById(id).get();
        incidencia.setNombre(incidenciaActualizada.getNombre());
        clasificacionIncidenciaRepository.save(incidencia); // Persistir los cambios
    }

    // Actualizar una clasificación de urgencia existente
    public void actualizarUrgencia(Long id, ClasificacionUrgencia urgenciaActualizada) {
        ClasificacionUrgencia urgencia = clasificacionUrgenciaRepository.findById(id).get();
        urgencia.setNombre(urgenciaActualizada.getNombre());
        clasificacionUrgenciaRepository.save(urgencia); // Persistir los cambios
    }
    // Actualizar una clasificación de servicio existente
    public void actualizarServicio(Long id, ClasificacionServicio servicioActualizado) {
        ClasificacionServicio servicio = clasificacionServicioRepository.findById(id).get();
        servicio.setNombre(servicioActualizado.getNombre());
        clasificacionServicioRepository.save(servicio); // Persistir los cambios
    }




    public void cambiarEstadoIncidencia(Long id, boolean isActive) {
        ClasificacionIncidencia incidencia = clasificacionIncidenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));
        incidencia.setIsActive(isActive);
        clasificacionIncidenciaRepository.save(incidencia);
    }

    public void cambiarEstadoServicio(Long id, boolean isActive) {
        ClasificacionServicio servicio = clasificacionServicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        servicio.setIsActive(isActive);
        clasificacionServicioRepository.save(servicio);
    }

    public void cambiarEstadoUrgencia(Long id, boolean isActive) {
        ClasificacionUrgencia urgencia = clasificacionUrgenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Urgencia no encontrada"));
        urgencia.setIsActive(isActive);
        clasificacionUrgenciaRepository.save(urgencia);
    }




}
