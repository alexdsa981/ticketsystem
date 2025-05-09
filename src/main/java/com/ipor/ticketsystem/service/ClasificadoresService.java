package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.fixed.*;
import com.ipor.ticketsystem.repository.fixed.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClasificadoresService {

    @Autowired
    private TipoIncidenciaRepository tipoIncidenciaRepository;

    @Autowired
    private SubCategoriaIncidenciaRepository subCategoriaIncidenciaRepository;

    @Autowired
    private CategoriaIncidenciaRepository categoriaIncidenciaRepository;

    @Autowired
    private ClasificacionAtencionRepository clasificacionAtencionRepository;

    @Autowired
    private ClasificacionUrgenciaRepository clasificacionUrgenciaRepository;

    @Autowired
    private ClasificacionDesestimacionRepository clasificacionDesestimacionRepository;

    @Autowired
    private AreaAtencionRepository areaAtencionRepository;

    //GUARDAR CLASIFICADOR EN BASE DE DATOS
    public void saveTIncidencia(TipoIncidencia tipoIncidencia) {
        tipoIncidenciaRepository.save(tipoIncidencia);
    }

    public void saveCUrgencia(ClasificacionUrgencia clasificacionUrgencia) {
        clasificacionUrgenciaRepository.save(clasificacionUrgencia);
    }

    public void saveCAtencion(ClasificacionAtencion clasificacionAtencion) {
        clasificacionAtencionRepository.save(clasificacionAtencion);
    }

    public void saveCDesestimacion(ClasificacionDesestimacion clasificacionDesestimacion) {
        clasificacionDesestimacionRepository.save(clasificacionDesestimacion);
    }

    public void saveCArea(AreaAtencion areaAtencion) {
        areaAtencionRepository.save(areaAtencion);
    }

    public void saveCatIncidencia(CategoriaIncidencia categoriaIncidencia) {
        categoriaIncidenciaRepository.save(categoriaIncidencia);
    }

    public void saveSubCatIncidencia(SubCategoriaIncidencia subCategoriaIncidencia) {
        subCategoriaIncidenciaRepository.save(subCategoriaIncidencia);
    }


    //LISTAR CLASIFICADORES SIN IMPORTAR EL ESTADO (ACTIVADO/DESACTIVADO)
    public List<CategoriaIncidencia> getListaCatIncidencia() {
        return categoriaIncidenciaRepository.findAllByOrderByNombreAsc();
    }
    public List<SubCategoriaIncidencia> getListaSubCatIncidencia() {
        return subCategoriaIncidenciaRepository.findAllByOrderByNombreAsc();
    }
    public List<TipoIncidencia> getListaTipoIncidencia() {
        return tipoIncidenciaRepository.findAllByOrderByNombreAsc();
    }

    //POR ID
    public List<SubCategoriaIncidencia> getListaSubCatIncidenciaPorIDCat(Long id) {
        return subCategoriaIncidenciaRepository.findByCategoriaIncidenciaIdOrderByNombreAsc(id);
    }
    public List<TipoIncidencia> getListaTipoIncidenciaPirIDSubCat(Long id) {
        return tipoIncidenciaRepository.findBySubCategoriaIncidenciaIdOrderByNombreAsc(id);
    }









    public List<ClasificacionUrgencia> getListaClasUrgencia() {

        return clasificacionUrgenciaRepository.findAll();
    }

    public List<ClasificacionAtencion> getListaClasAtencion() {

        return clasificacionAtencionRepository.findAllByOrderByNombreAsc();
    }

    public List<ClasificacionDesestimacion> getListaClasDesestimacion() {
        return clasificacionDesestimacionRepository.findAllByOrderByNombreAsc();
    }

    public List<AreaAtencion> getListaAreaAtencion() {
        return areaAtencionRepository.findAllByOrderByNombreAsc();
    }




    //OBTENER CLASIFICADORES POR ID
    public TipoIncidencia getTipoIncidenciaPorID(Long id) {
        return tipoIncidenciaRepository.findById(id).get();
    }

    public SubCategoriaIncidencia getSubCatIncidenciaPorID(Long id) {
        return subCategoriaIncidenciaRepository.findById(id).get();
    }

    public CategoriaIncidencia getCatIncidenciaPorID(Long id) {
        return categoriaIncidenciaRepository.findById(id).get();
    }

    public ClasificacionUrgencia getClasificacionUrgenciaPorId(Long id) {
        return clasificacionUrgenciaRepository.findById(id).get();
    }

    public ClasificacionAtencion getClasificacionAtencionPorId(Long id) {
        return clasificacionAtencionRepository.findById(id).get();
    }

    public ClasificacionDesestimacion getClasificacionDesestimacionPorId(Long id) {
        return clasificacionDesestimacionRepository.findById(id).get();
    }

    public AreaAtencion getAreaAtencionPorId(Long id) {
        return areaAtencionRepository.findById(id).get();
    }


    //LISTAR CLASIFICADORES ESTADO (ACTIVO)
    public List<TipoIncidencia> getListaTiposDeIncidenciaActivos() {
        return tipoIncidenciaRepository.findByIsActiveTrue();
    }
    public List<SubCategoriaIncidencia> getListaSubCatIncidenciaActivos() {
        return subCategoriaIncidenciaRepository.findByIsActiveTrue();
    }
    public List<CategoriaIncidencia> getListaCatIncidenciaActivos() {
        return categoriaIncidenciaRepository.findByIsActiveTrue();
    }

    public List<ClasificacionUrgencia> getListaClasificacionUrgenciaActivos() {
        return clasificacionUrgenciaRepository.findByIsActiveTrue();
    }

    public List<ClasificacionAtencion> getListaClasificacionAtencionActivos() {
        return clasificacionAtencionRepository.findByIsActiveTrue();
    }

    public List<ClasificacionDesestimacion> getListaClasificacionDesestimacionActivos() {
        return clasificacionDesestimacionRepository.findByIsActiveTrue();
    }

    public List<AreaAtencion> getListaAreaAtencionActivos() {
        return areaAtencionRepository.findByIsActiveTrueOrderByNombreAsc();
    }


    // MODIFICAR NOMBRE DE UN CLASIFICADOR EXISTENTE
    public void actualizarTipoIncidencia(Long id, TipoIncidencia incidenciaActualizada) {
        TipoIncidencia incidencia = tipoIncidenciaRepository.findById(id).get();
        incidencia.setNombre(incidenciaActualizada.getNombre());
        incidencia.setSubCategoriaIncidencia(incidenciaActualizada.getSubCategoriaIncidencia());
        tipoIncidenciaRepository.save(incidencia);
    }

    public void actualizarSubCatIncidencia(Long id, SubCategoriaIncidencia subCatIncidenciaActualizada) {
        SubCategoriaIncidencia incidencia = subCategoriaIncidenciaRepository.findById(id).get();
        incidencia.setNombre(subCatIncidenciaActualizada.getNombre());
        incidencia.setCategoriaIncidencia(subCatIncidenciaActualizada.getCategoriaIncidencia());
        subCategoriaIncidenciaRepository.save(incidencia);
    }

    public void actualizarCatIncidencia(Long id, CategoriaIncidencia CatIncidenciaActualizada) {
        CategoriaIncidencia incidencia = categoriaIncidenciaRepository.findById(id).get();
        incidencia.setNombre(CatIncidenciaActualizada.getNombre());
        categoriaIncidenciaRepository.save(incidencia);
    }

    public void actualizarUrgencia(Long id, ClasificacionUrgencia urgenciaActualizada) {
        ClasificacionUrgencia urgencia = clasificacionUrgenciaRepository.findById(id).get();
        urgencia.setNombre(urgenciaActualizada.getNombre());
        clasificacionUrgenciaRepository.save(urgencia); // Persistir los cambios
    }

    public void actualizarAtencion(Long id, ClasificacionAtencion atencionActualizado) {
        ClasificacionAtencion atencion = clasificacionAtencionRepository.findById(id).get();
        atencion.setNombre(atencionActualizado.getNombre());
        clasificacionAtencionRepository.save(atencion); // Persistir los cambios
    }

    public void actualizarDesestimacion(Long id, ClasificacionDesestimacion desestimacionActualizado) {
        ClasificacionDesestimacion desestimacion = clasificacionDesestimacionRepository.findById(id).get();
        desestimacion.setNombre(desestimacionActualizado.getNombre());
        clasificacionDesestimacionRepository.save(desestimacion); // Persistir los cambios
    }

    public void actualizarArea(Long id, AreaAtencion areaActualizada) {
        AreaAtencion area = areaAtencionRepository.findById(id).get();
        area.setNombre(areaActualizada.getNombre());
        areaAtencionRepository.save(area); // Persistir los cambios
    }

    //CAMBIAR ESTADO DE UN CLASIFICADOR EXISTENTE (ACTIVADO/DESACTIVADO)
    public void cambiarEstadoIncidencia(Long id, boolean isActive) {
        TipoIncidencia incidencia = tipoIncidenciaRepository.findById(id).orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));
        incidencia.setIsActive(isActive);
        tipoIncidenciaRepository.save(incidencia);
    }

    public void cambiarEstadoSubCatIncidencia(Long id, boolean isActive) {
        SubCategoriaIncidencia incidencia = subCategoriaIncidenciaRepository.findById(id).orElseThrow(() -> new RuntimeException("Sub Categoria Incidencia no encontrada"));
        incidencia.setIsActive(isActive);
        subCategoriaIncidenciaRepository.save(incidencia);
    }

    public void cambiarEstadoCatIncidencia(Long id, boolean isActive) {
        CategoriaIncidencia incidencia = categoriaIncidenciaRepository.findById(id).orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));
        incidencia.setIsActive(isActive);
        categoriaIncidenciaRepository.save(incidencia);
    }

    public void cambiarEstadoAtencion(Long id, boolean isActive) {
        ClasificacionAtencion atencion = clasificacionAtencionRepository.findById(id).orElseThrow(() -> new RuntimeException("Atencion no encontrado"));
        atencion.setIsActive(isActive);
        clasificacionAtencionRepository.save(atencion);
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
        AreaAtencion area = areaAtencionRepository.findById(id).orElseThrow(() -> new RuntimeException("Area no encontrada"));
        area.setIsActive(isActive);
        areaAtencionRepository.save(area);
    }









    public List<SubCategoriaIncidencia> getListaSubCatIncidenciaActivosPorIDCat(Long idCategoria) {
        return subCategoriaIncidenciaRepository.findByCategoriaIncidenciaIdAndIsActiveTrue(idCategoria);
    }

    public List<TipoIncidencia> getListaTiposDeIncidenciaActivosPorIDSubCat(Long idSubCategoria) {
        return tipoIncidenciaRepository.findBySubCategoriaIncidenciaIdAndIsActiveTrue(idSubCategoria);
    }







}
