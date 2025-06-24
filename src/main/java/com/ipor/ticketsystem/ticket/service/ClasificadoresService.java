package com.ipor.ticketsystem.ticket.service;

import com.ipor.ticketsystem.ticket.comun.HorarioAtencionSoporte;
import com.ipor.ticketsystem.ticket.comun.HorarioAtencionSoporteRepository;
import com.ipor.ticketsystem.usuario.UsuarioService;
import com.ipor.ticketsystem.ticket.clasificadores.model.*;
import com.ipor.ticketsystem.ticket.clasificadores.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @Autowired
    private SedeRepository sedeRepository;

    @Autowired
    private HorarioAtencionSoporteRepository horarioAtencionSoporteRepository;

    @Autowired
    private ClasificacionEsperaRepository clasificacionEsperaRepository;

    @Autowired
    private UsuarioService usuarioService;



    //Espera---------------------------------------------------------------


    public void saveCEspera(ClasificacionEspera clasificacionEspera) {
        clasificacionEsperaRepository.save(clasificacionEspera);
    }
    public List<ClasificacionEspera> getListaClasEspera() {
        return clasificacionEsperaRepository.findAllExceptId1();
    }
    public ClasificacionEspera getClasificacionEsperaPorId(Long id) {
        return clasificacionEsperaRepository.findById(id).get();
    }

    public List<ClasificacionEspera> getListaClasificacionEsperaActivos() {
        return clasificacionEsperaRepository.findByIsActiveTrueAndIdNot1();
    }
    public void actualizarEspera(Long id, ClasificacionEspera esperaActualizada) {
        ClasificacionEspera espera = clasificacionEsperaRepository.findById(id).get();
        espera.setNombre(esperaActualizada.getNombre());
        clasificacionEsperaRepository.save(espera); // Persistir los cambios
    }
    public void cambiarEstadoEspera(Long id, boolean isActive) {
        ClasificacionEspera espera = clasificacionEsperaRepository.findById(id).orElseThrow(() -> new RuntimeException("Espera no encontrada"));
        espera.setIsActive(isActive);
        clasificacionEsperaRepository.save(espera);
    }

    //HORARIO DE ATENCON
    public void nuevoHorario(LocalTime horaInicio, LocalTime horaFin) {
        HorarioAtencionSoporte horarioNuevo = new HorarioAtencionSoporte();
        horarioNuevo.setHoraEntrada(horaInicio);
        horarioNuevo.setHoraSalida(horaFin);
        horarioNuevo.setFechaHoraCreacion(LocalDateTime.now());
        horarioNuevo.setUsuarioCreacion(usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado()));
        horarioAtencionSoporteRepository.save(horarioNuevo);
    }
    public HorarioAtencionSoporte getLastHorarioAtencionSoporte(){
        return horarioAtencionSoporteRepository.findTopByOrderByIdDesc();
    }

    //URGENCIA---------------------------------------------------------------


    public void saveCUrgencia(ClasificacionUrgencia clasificacionUrgencia) {
        clasificacionUrgenciaRepository.save(clasificacionUrgencia);
    }
    public List<ClasificacionUrgencia> getListaClasUrgencia() {
        return clasificacionUrgenciaRepository.findAll();
    }
    public ClasificacionUrgencia getClasificacionUrgenciaPorId(Long id) {
        return clasificacionUrgenciaRepository.findById(id).get();
    }
    public List<ClasificacionUrgencia> getListaClasificacionUrgenciaActivos() {
        return clasificacionUrgenciaRepository.findByIsActiveTrue();
    }
    public void actualizarUrgencia(Long id, ClasificacionUrgencia urgenciaActualizada) {
        ClasificacionUrgencia urgencia = clasificacionUrgenciaRepository.findById(id).get();
        urgencia.setNombre(urgenciaActualizada.getNombre());
        clasificacionUrgenciaRepository.save(urgencia); // Persistir los cambios
    }
    public void cambiarEstadoUrgencia(Long id, boolean isActive) {
        ClasificacionUrgencia urgencia = clasificacionUrgenciaRepository.findById(id).orElseThrow(() -> new RuntimeException("Urgencia no encontrada"));
        urgencia.setIsActive(isActive);
        clasificacionUrgenciaRepository.save(urgencia);
    }

    //DESESTIMACION---------------------------------------------------------------


    public void saveCDesestimacion(ClasificacionDesestimacion clasificacionDesestimacion) {
        clasificacionDesestimacionRepository.save(clasificacionDesestimacion);
    }
    public List<ClasificacionDesestimacion> getListaClasDesestimacion() {
        return clasificacionDesestimacionRepository.findAllByOrderByNombreAsc();
    }
    public ClasificacionDesestimacion getClasificacionDesestimacionPorId(Long id) {
        return clasificacionDesestimacionRepository.findById(id).get();
    }
    public List<ClasificacionDesestimacion> getListaClasificacionDesestimacionActivos() {
        return clasificacionDesestimacionRepository.findByIsActiveTrue();
    }
    public void actualizarDesestimacion(Long id, ClasificacionDesestimacion desestimacionActualizado) {
        ClasificacionDesestimacion desestimacion = clasificacionDesestimacionRepository.findById(id).get();
        desestimacion.setNombre(desestimacionActualizado.getNombre());
        clasificacionDesestimacionRepository.save(desestimacion); // Persistir los cambios
    }
    public void cambiarEstadoDesestimacion(Long id, boolean isActive) {
        ClasificacionDesestimacion desestimacion = clasificacionDesestimacionRepository.findById(id).orElseThrow(() -> new RuntimeException("Desestimacion no encontrada"));
        desestimacion.setIsActive(isActive);
        clasificacionDesestimacionRepository.save(desestimacion);
    }

    //ATENCION---------------------------------------------------------------


    public void saveCAtencion(ClasificacionAtencion clasificacionAtencion) {
        clasificacionAtencionRepository.save(clasificacionAtencion);
    }
    public List<ClasificacionAtencion> getListaClasAtencion() {
        return clasificacionAtencionRepository.findAllByOrderByNombreAsc();
    }
    public ClasificacionAtencion getClasificacionAtencionPorId(Long id) {
        return clasificacionAtencionRepository.findById(id).get();
    }
    public List<ClasificacionAtencion> getListaClasificacionAtencionActivos() {
        return clasificacionAtencionRepository.findByIsActiveTrue();
    }
    public void actualizarAtencion(Long id, ClasificacionAtencion atencionActualizado) {
        ClasificacionAtencion atencion = clasificacionAtencionRepository.findById(id).get();
        atencion.setNombre(atencionActualizado.getNombre());
        clasificacionAtencionRepository.save(atencion); // Persistir los cambios
    }
    public void cambiarEstadoAtencion(Long id, boolean isActive) {
        ClasificacionAtencion atencion = clasificacionAtencionRepository.findById(id).orElseThrow(() -> new RuntimeException("Atencion no encontrado"));
        atencion.setIsActive(isActive);
        clasificacionAtencionRepository.save(atencion);
    }

    //AREA---------------------------------------------------------------


    public void saveSede(Sede sede) {
        sedeRepository.save(sede);
    }

    public void saveCArea(AreaAtencion areaAtencion) {
        areaAtencionRepository.save(areaAtencion);
    }
    public List<AreaAtencion> getListaAreaAtencion() {
        return areaAtencionRepository.findAllByOrderByNombreAsc();
    }
    public List<Sede> getListaSedes() {
        return sedeRepository.findAllByOrderByNombreAsc();
    }
    public AreaAtencion getAreaAtencionPorId(Long id) {
        return areaAtencionRepository.findById(id).get();
    }
    public Sede getSedePorId(Long id) {
        return sedeRepository.findById(id).get();
    }

    public List<AreaAtencion> getListaAreaPorSedeId(Long id) {
        return areaAtencionRepository.findBySedeIdOrderByNombreAsc(id);
    }
    public List<Sede> getListaSedesActivos() {
        return sedeRepository.findByIsActiveTrueOrderByNombreAsc();
    }
    public void actualizarArea(Long id, AreaAtencion areaActualizada) {
        AreaAtencion area = areaAtencionRepository.findById(id).get();
        area.setNombre(areaActualizada.getNombre());
        area.setSede(areaActualizada.getSede());
        areaAtencionRepository.save(area); // Persistir los cambios
    }
    public void actualizarSede(Long id, Sede sedeActualizada) {
        Sede sede = sedeRepository.findById(id).get();
        sede.setNombre(sedeActualizada.getNombre());
        sedeRepository.save(sede); // Persistir los cambios
    }
    public void cambiarEstadoArea(Long id, boolean isActive) {
        AreaAtencion area = areaAtencionRepository.findById(id).orElseThrow(() -> new RuntimeException("Area no encontrada"));
        area.setIsActive(isActive);
        areaAtencionRepository.save(area);
    }
    public void cambiarEstadoSede(Long id, boolean isActive) {
        Sede sede = sedeRepository.findById(id).orElseThrow(() -> new RuntimeException("Sede no encontrada"));
        sede.setIsActive(isActive);
        sedeRepository.save(sede);
    }
    public List<AreaAtencion> getListaAreasActivosPorIDSede(Long idSede) {
        return areaAtencionRepository.findBySedeIdAndIsActiveTrueOrderByNombreAsc(idSede);
    }



    //INCIDENCIA---------------------------------------------------------------


    public void saveCatIncidencia(CategoriaIncidencia categoriaIncidencia) {
        categoriaIncidenciaRepository.save(categoriaIncidencia);
    }

    public void saveSubCatIncidencia(SubCategoriaIncidencia subCategoriaIncidencia) {
        subCategoriaIncidenciaRepository.save(subCategoriaIncidencia);
    }
    public void saveTIncidencia(TipoIncidencia tipoIncidencia) {
        tipoIncidenciaRepository.save(tipoIncidencia);
    }
    public List<CategoriaIncidencia> getListaCatIncidencia() {
        return categoriaIncidenciaRepository.findAllByOrderByNombreAsc();
    }
    public List<SubCategoriaIncidencia> getListaSubCatIncidencia() {
        return subCategoriaIncidenciaRepository.findAllByOrderByNombreAsc();
    }
    public List<TipoIncidencia> getListaTipoIncidencia() {
        return tipoIncidenciaRepository.findAllByOrderByNombreAsc();
    }
    public List<SubCategoriaIncidencia> getListaSubCatIncidenciaPorIDCat(Long id) {
        return subCategoriaIncidenciaRepository.findByCategoriaIncidenciaIdOrderByNombreAsc(id);
    }
    public List<TipoIncidencia> getListaTipoIncidenciaPirIDSubCat(Long id) {
        return tipoIncidenciaRepository.findBySubCategoriaIncidenciaIdOrderByNombreAsc(id);
    }
    public TipoIncidencia getTipoIncidenciaPorID(Long id) {
        return tipoIncidenciaRepository.findById(id).get();
    }
    public SubCategoriaIncidencia getSubCatIncidenciaPorID(Long id) {
        return subCategoriaIncidenciaRepository.findById(id).get();
    }
    public CategoriaIncidencia getCatIncidenciaPorID(Long id) {
        return categoriaIncidenciaRepository.findById(id).get();
    }
    public List<TipoIncidencia> getListaTiposDeIncidenciaActivos() {
        return tipoIncidenciaRepository.findByIsActiveTrue();
    }
    public List<SubCategoriaIncidencia> getListaSubCatIncidenciaActivos() {
        return subCategoriaIncidenciaRepository.findByIsActiveTrue();
    }
    public List<CategoriaIncidencia> getListaCatIncidenciaActivos() {
        return categoriaIncidenciaRepository.findByIsActiveTrue();
    }
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
    public List<SubCategoriaIncidencia> getListaSubCatIncidenciaActivosPorIDCat(Long idCategoria) {
        return subCategoriaIncidenciaRepository.findByCategoriaIncidenciaIdAndIsActiveTrue(idCategoria);
    }

    public List<TipoIncidencia> getListaTiposDeIncidenciaActivosPorIDSubCat(Long idSubCategoria) {
        return tipoIncidenciaRepository.findBySubCategoriaIncidenciaIdAndIsActiveTrue(idSubCategoria);
    }



















}
