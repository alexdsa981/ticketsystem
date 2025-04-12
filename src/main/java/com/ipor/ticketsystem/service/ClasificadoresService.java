package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.fixed.*;
import com.ipor.ticketsystem.repository.fixed.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClasificadoresService {

    @Autowired
    private ClasificacionIncidenciaRepository clasificacionIncidenciaRepository;

    @Autowired
    private ClasificacionServicioRepository clasificacionServicioRepository;

    @Autowired
    private ClasificacionUrgenciaRepository clasificacionUrgenciaRepository;

    @Autowired
    private ClasificacionDesestimacionRepository clasificacionDesestimacionRepository;

    @Autowired
    private ClasificacionAreaRepository clasificacionAreaRepository;

    //GUARDAR CLASIFICADOR EN BASE DE DATOS
    public void saveCIncidencia(ClasificacionIncidencia clasificacionIncidencia) {
        clasificacionIncidenciaRepository.save(clasificacionIncidencia);
    }

    public void saveCUrgencia(ClasificacionUrgencia clasificacionUrgencia) {
        clasificacionUrgenciaRepository.save(clasificacionUrgencia);
    }

    public void saveCServicio(ClasificacionServicio clasificacionServicio) {
        clasificacionServicioRepository.save(clasificacionServicio);
    }

    public void saveCDesestimacion(ClasificacionDesestimacion clasificacionDesestimacion) {
        clasificacionDesestimacionRepository.save(clasificacionDesestimacion);
    }

    public void saveCArea(ClasificacionArea clasificacionArea) {
        clasificacionAreaRepository.save(clasificacionArea);
    }


    //LISTAR CLASIFICADORES SIN IMPORTAR EL ESTADO (ACTIVADO/DESACTIVADO)

    public List<ClasificacionIncidencia> getListaClasIncidencia() {
        return clasificacionIncidenciaRepository.findAll();
    }

    public List<ClasificacionUrgencia> getListaClasUrgencia() {

        return clasificacionUrgenciaRepository.findAll();
    }

    public List<ClasificacionServicio> getListaClasServicio() {

        return clasificacionServicioRepository.findAll();
    }

    public List<ClasificacionDesestimacion> getListaClasDesestimacion() {
        return clasificacionDesestimacionRepository.findAll();
    }

    public List<ClasificacionArea> getListaClasArea() {
        return clasificacionAreaRepository.findAll();
    }

    //OBTENER CLASIFICADORES POR ID
    public ClasificacionIncidencia getClasificacionIncidenciaPorID(Long id) {
        return clasificacionIncidenciaRepository.findById(id).get();
    }

    public ClasificacionUrgencia getClasificacionUrgenciaPorId(Long id) {
        return clasificacionUrgenciaRepository.findById(id).get();
    }

    public ClasificacionServicio getClasificacionServicioPorId(Long id) {
        return clasificacionServicioRepository.findById(id).get();
    }

    public ClasificacionDesestimacion getClasificacionDesestimacionPorId(Long id) {
        return clasificacionDesestimacionRepository.findById(id).get();
    }

    public ClasificacionArea getClasificacionAreaPorId(Long id) {
        return clasificacionAreaRepository.findById(id).get();
    }


    //LISTAR CLASIFICADORES ESTADO (ACTIVO)
    public List<ClasificacionIncidencia> getListaTiposDeIncidenciaActivos() {
        return clasificacionIncidenciaRepository.findByIsActiveTrue();
    }

    public List<ClasificacionUrgencia> getListaClasificacionUrgenciaActivos() {
        return clasificacionUrgenciaRepository.findByIsActiveTrue();
    }

    public List<ClasificacionServicio> getListaClasificacionServicioActivos() {
        return clasificacionServicioRepository.findByIsActiveTrue();
    }

    public List<ClasificacionDesestimacion> getListaClasificacionDesestimacionActivos() {
        return clasificacionDesestimacionRepository.findByIsActiveTrue();
    }

    public List<ClasificacionArea> getListaClasificacionAreaActivos() {
        return clasificacionAreaRepository.findByIsActiveTrue();
    }


    // MODIFICAR NOMBRE DE UN CLASIFICADOR EXISTENTE
    public void actualizarIncidencia(Long id, ClasificacionIncidencia incidenciaActualizada) {
        ClasificacionIncidencia incidencia = clasificacionIncidenciaRepository.findById(id).get();
        incidencia.setNombre(incidenciaActualizada.getNombre());
        clasificacionIncidenciaRepository.save(incidencia); // Persistir los cambios
    }

    public void actualizarUrgencia(Long id, ClasificacionUrgencia urgenciaActualizada) {
        ClasificacionUrgencia urgencia = clasificacionUrgenciaRepository.findById(id).get();
        urgencia.setNombre(urgenciaActualizada.getNombre());
        clasificacionUrgenciaRepository.save(urgencia); // Persistir los cambios
    }

    public void actualizarServicio(Long id, ClasificacionServicio servicioActualizado) {
        ClasificacionServicio servicio = clasificacionServicioRepository.findById(id).get();
        servicio.setNombre(servicioActualizado.getNombre());
        clasificacionServicioRepository.save(servicio); // Persistir los cambios
    }

    public void actualizarDesestimacion(Long id, ClasificacionDesestimacion desestimacionActualizado) {
        ClasificacionDesestimacion desestimacion = clasificacionDesestimacionRepository.findById(id).get();
        desestimacion.setNombre(desestimacionActualizado.getNombre());
        clasificacionDesestimacionRepository.save(desestimacion); // Persistir los cambios
    }

    public void actualizarArea(Long id, ClasificacionArea areaActualizada) {
        ClasificacionArea area = clasificacionAreaRepository.findById(id).get();
        area.setNombre(areaActualizada.getNombre());
        clasificacionAreaRepository.save(area); // Persistir los cambios
    }

    //CAMBIAR ESTADO DE UN CLASIFICADOR EXISTENTE (ACTIVADO/DESACTIVADO)
    public void cambiarEstadoIncidencia(Long id, boolean isActive) {
        ClasificacionIncidencia incidencia = clasificacionIncidenciaRepository.findById(id).orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));
        incidencia.setIsActive(isActive);
        clasificacionIncidenciaRepository.save(incidencia);
    }

    public void cambiarEstadoServicio(Long id, boolean isActive) {
        ClasificacionServicio servicio = clasificacionServicioRepository.findById(id).orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
        servicio.setIsActive(isActive);
        clasificacionServicioRepository.save(servicio);
    }

    public void cambiarEstadoUrgencia(Long id, boolean isActive) {
        ClasificacionUrgencia urgencia = clasificacionUrgenciaRepository.findById(id).orElseThrow(() -> new RuntimeException("Urgencia no encontrada"));
        urgencia.setIsActive(isActive);
        clasificacionUrgenciaRepository.save(urgencia);
    }

    public void cambiarEstadoDesestimacion(Long id, boolean isActive) {
        ClasificacionDesestimacion desestimacion = clasificacionDesestimacionRepository.findById(id).orElseThrow(() -> new RuntimeException("Desestimacion no encontrada"));
        desestimacion.setIsActive(isActive);
        clasificacionDesestimacionRepository.save(desestimacion);
    }

    public void cambiarEstadoArea(Long id, boolean isActive) {
        ClasificacionArea area = clasificacionAreaRepository.findById(id).orElseThrow(() -> new RuntimeException("Area no encontrada"));
        area.setIsActive(isActive);
        clasificacionAreaRepository.save(area);
    }

}
