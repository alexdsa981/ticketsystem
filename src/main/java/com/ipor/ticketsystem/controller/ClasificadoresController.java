package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.fixed.*;
import com.ipor.ticketsystem.service.ClasificadoresService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/app/clasificadores")
public class ClasificadoresController {
    @Autowired
    ClasificadoresService clasificadoresService;


    // Listar todos los clasificadores (Incidencias, Atencion, Urgencias)

    public Model listarClasificadores(Model model) {
        List<TipoIncidencia> tipoIncidencias = clasificadoresService.getListaTipoIncidencia();
        List<SubCategoriaIncidencia> subCatIncidencias = clasificadoresService.getListaSubCatIncidencia();
        List<CategoriaIncidencia> catIncidencias = clasificadoresService.getListaCatIncidencia();
        List<ClasificacionAtencion> atencions = clasificadoresService.getListaClasAtencion();
        List<ClasificacionUrgencia> urgencias = clasificadoresService.getListaClasUrgencia();
        List<ClasificacionDesestimacion> desestimaciones = clasificadoresService.getListaClasDesestimacion();
        List<AreaAtencion> areas = clasificadoresService.getListaAreaAtencion();
        model.addAttribute("desestimaciones", desestimaciones);
        model.addAttribute("subCatIncidencias", subCatIncidencias);
        model.addAttribute("catIncidencias", catIncidencias);
        model.addAttribute("tipoIncidencias", tipoIncidencias);
        model.addAttribute("atenciones", atencions);
        model.addAttribute("urgencias", urgencias);
        model.addAttribute("areas", areas);
        return model;
    }

    public Model getListaTipoIncidenciaActivos(Model model) {
        List<TipoIncidencia> ListaTiposIncidencia = clasificadoresService.getListaTiposDeIncidenciaActivos();
        model.addAttribute("Lista_tipo_incidencia", ListaTiposIncidencia);
        return model;
    }

    public Model getListaSubCatIncidenciaActivos(Model model) {
        List<SubCategoriaIncidencia> ListaSubCatIncidencia = clasificadoresService.getListaSubCatIncidenciaActivos();
        model.addAttribute("Lista_subcat_incidencia", ListaSubCatIncidencia);
        return model;
    }

    public Model getListaCatIncidenciaActivos(Model model) {
        List<CategoriaIncidencia> ListaCatIncidencia = clasificadoresService.getListaCatIncidenciaActivos();
        model.addAttribute("Lista_cat_incidencia", ListaCatIncidencia);
        return model;
    }

    public Model getListaClasificacionesUrgenciaActivos(Model model){
        List<ClasificacionUrgencia> listaUrgencias = clasificadoresService.getListaClasificacionUrgenciaActivos();
        model.addAttribute("Lista_clasificacion_urgencia", listaUrgencias);
        return  model;
    }
    public Model getListaClasificacionesAtencionActivos(Model model){
        List<ClasificacionAtencion> listaAtencions = clasificadoresService.getListaClasificacionAtencionActivos();
        model.addAttribute("Lista_clasificacion_atencion", listaAtencions);
        return  model;
    }
    public Model getListaClasificacionesDesestimacionActivos(Model model){
        List<ClasificacionDesestimacion> listaDesestimacion = clasificadoresService.getListaClasificacionDesestimacionActivos();
        model.addAttribute("Lista_clasificacion_desestimacion", listaDesestimacion);
        return  model;
    }
    public Model getListaClasificacionesAreaActivos(Model model){
        List<AreaAtencion> listaArea = clasificadoresService.getListaAreaAtencionActivos();
        model.addAttribute("Lista_area_atencion", listaArea);
        return  model;
    }



    //crear incidencia nueva
    @PostMapping("/area/nuevo")
    public ResponseEntity<String> crearClasificacionAr(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        AreaAtencion areaAtencion = new AreaAtencion();
        areaAtencion.setNombre(nombre);
        areaAtencion.setIsActive(Boolean.TRUE);
        clasificadoresService.saveCArea(areaAtencion);
        response.sendRedirect("/admin/Clasificadores");
        return ResponseEntity.ok("Clasificación Area creado correctamente");
    }

    // Actualizar un incidencia existente
    @PostMapping("/actualizar/area/{id}")
    public String actualizarArea(@PathVariable Long id,
                                       @RequestParam("nombre") String nombre
    ) {
        AreaAtencion areaAtencion = new AreaAtencion();
        areaAtencion.setNombre(nombre);
        areaAtencion.setIsActive(Boolean.TRUE);
        clasificadoresService.actualizarArea(id, areaAtencion);
        return "redirect:/admin/Clasificadores";
    }



    //crear desestimacion nueva
    @PostMapping("/desestimacion/nuevo")
    public ResponseEntity<String> crearClasificacionDesestimacion(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        ClasificacionDesestimacion clasificacionDesestimacion = new ClasificacionDesestimacion();
        clasificacionDesestimacion.setNombre(nombre);
        clasificacionDesestimacion.setIsActive(Boolean.TRUE);
        clasificadoresService.saveCDesestimacion(clasificacionDesestimacion);
        response.sendRedirect("/admin/Clasificadores");
        return ResponseEntity.ok("Clasificación Desestimacion creado correctamente");
    }
    // Actualizar un desestimacion existente
    @PostMapping("/actualizar/desestimacion/{id}")
    public String actualizarDesestimacion(@PathVariable Long id,
                                       @RequestParam("nombre") String nombre
    ) {
        ClasificacionDesestimacion clasificacionDesestimacion = new ClasificacionDesestimacion();
        clasificacionDesestimacion.setNombre(nombre);
        clasificacionDesestimacion.setIsActive(Boolean.TRUE);
        clasificadoresService.actualizarDesestimacion(id, clasificacionDesestimacion);
        return "redirect:/admin/Clasificadores";
    }



    //crear incidencia nueva
    @PostMapping("/tipoIncidencia/nuevo")
    public ResponseEntity<String> crearTipoIncidenciaa(
            @RequestParam("nombre") String nombre,
            @RequestParam("id_subcat") Long id_subcat,
            HttpServletResponse response) throws IOException {
        TipoIncidencia tipoIncidencia = new TipoIncidencia();
        tipoIncidencia.setNombre(nombre);
        tipoIncidencia.setSubcategoria(clasificadoresService.getSubCatIncidenciaPorID(id_subcat));
        tipoIncidencia.setIsActive(Boolean.TRUE);
        clasificadoresService.saveTIncidencia(tipoIncidencia);
        response.sendRedirect("/admin/Clasificadores");
        return ResponseEntity.ok("Clasificación Incidencia creado correctamente");
    }

    // Actualizar un incidencia existente
    @PostMapping("/actualizar/tipoIncidencia/{id}")
    public String actualizarTipoIncidencia(@PathVariable Long id,
                                           @RequestParam("id_subcat") Long id_subcat,
                                           @RequestParam("nombre") String nombre) {
        TipoIncidencia tipoIncidencia = new TipoIncidencia();
        tipoIncidencia.setNombre(nombre);
        tipoIncidencia.setSubcategoria(clasificadoresService.getSubCatIncidenciaPorID(id_subcat));
        tipoIncidencia.setIsActive(Boolean.TRUE);
        clasificadoresService.actualizarTipoIncidencia(id, tipoIncidencia);
        return "redirect:/admin/Clasificadores";
    }


    //crear incidencia nueva
    @PostMapping("/subCatIncidencia/nuevo")
    public ResponseEntity<String> crearSubCatIncidenciaa(
            @RequestParam("id_cat") Long id_cat,
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        SubCategoriaIncidencia sbCategoriaIncidencia = new SubCategoriaIncidencia();
        sbCategoriaIncidencia.setNombre(nombre);
        sbCategoriaIncidencia.setCategoria(clasificadoresService.getCatIncidenciaPorID(id_cat));
        sbCategoriaIncidencia.setIsActive(Boolean.TRUE);
        clasificadoresService.saveSubCatIncidencia(sbCategoriaIncidencia);
        response.sendRedirect("/admin/Clasificadores");
        return ResponseEntity.ok("Clasificación sbCategoriaIncidencia creado correctamente");
    }

    // Actualizar un incidencia existente
    @PostMapping("/actualizar/subCatIncidencia/{id}")
    public String actualizarIncidencia(@PathVariable Long id,
                                       @RequestParam("id_cat") Long id_cat,
                                       @RequestParam("nombre") String nombre) {
        SubCategoriaIncidencia sbCategoriaIncidencia = new SubCategoriaIncidencia();
        sbCategoriaIncidencia.setNombre(nombre);
        sbCategoriaIncidencia.setCategoria(clasificadoresService.getCatIncidenciaPorID(id_cat));
        sbCategoriaIncidencia.setIsActive(Boolean.TRUE);
        clasificadoresService.actualizarSubCatIncidencia(id, sbCategoriaIncidencia);
        return "redirect:/admin/Clasificadores";
    }



    //crear incidencia nueva
    @PostMapping("/catIncidencia/nuevo")
    public ResponseEntity<String> crearTipoIncidenciaa(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        CategoriaIncidencia categoriaIncidencia = new CategoriaIncidencia();
        categoriaIncidencia.setNombre(nombre);
        categoriaIncidencia.setIsActive(Boolean.TRUE);
        clasificadoresService.saveCatIncidencia(categoriaIncidencia);
        response.sendRedirect("/admin/Clasificadores");
        return ResponseEntity.ok("Clasificación Incidencia creado correctamente");
    }

    // Actualizar un incidencia existente
    @PostMapping("/actualizar/catIncidencia/{id}")
    public String actualizarIncidencia(@PathVariable Long id,
                                       @RequestParam("nombre") String nombre
    ) {
        CategoriaIncidencia categoriaIncidencia = new CategoriaIncidencia();
        categoriaIncidencia.setNombre(nombre);
        categoriaIncidencia.setIsActive(Boolean.TRUE);
        clasificadoresService.actualizarCatIncidencia(id, categoriaIncidencia);
        return "redirect:/admin/Clasificadores";
    }


    //crear Clasificacion atencion nueva
    @PostMapping("/atencion/nuevo")
    public ResponseEntity<String> crearClasificacionAtencion(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        ClasificacionAtencion clasificacionAtencion = new ClasificacionAtencion();
        clasificacionAtencion.setNombre(nombre);
        clasificacionAtencion.setIsActive(Boolean.TRUE);
        clasificadoresService.saveCAtencion(clasificacionAtencion);
        response.sendRedirect("/admin/Clasificadores");
        return ResponseEntity.ok("Clasificación Atencion creado correctamente");
    }

    // Actualizar un atencion existente
    @PostMapping("/actualizar/atencion/{id}")
    public String actualizarAtencion(@PathVariable Long id,
                                       @RequestParam("nombre") String nombre
    ) {
        ClasificacionAtencion clasificacionAtencion = new ClasificacionAtencion();
        clasificacionAtencion.setNombre(nombre);
        clasificacionAtencion.setIsActive(Boolean.TRUE);
        clasificadoresService.actualizarAtencion(id, clasificacionAtencion);
        return "redirect:/admin/Clasificadores";
    }

    //crear Clasificacion Urgencia nueva
    @PostMapping("/urgencia/nuevo")
    public ResponseEntity<String> crearClasificacionUrgencia(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        ClasificacionUrgencia clasificacionUrgencia = new ClasificacionUrgencia();
        clasificacionUrgencia.setNombre(nombre);
        clasificacionUrgencia.setIsActive(Boolean.TRUE);
        clasificadoresService.saveCUrgencia(clasificacionUrgencia);
        response.sendRedirect("/admin/Clasificadores");
        return ResponseEntity.ok("Clasificación Urgencia creado correctamente");
    }

    // Actualizar un Urgencia existente
    @PostMapping("/actualizar/urgencia/{id}")
    public String actualizarUrgencia(@PathVariable Long id,
                                     @RequestParam("nombre") String nombre
    ) {
        ClasificacionUrgencia clasificacionUrgencia = new ClasificacionUrgencia();
        clasificacionUrgencia.setNombre(nombre);
        clasificacionUrgencia.setIsActive(Boolean.TRUE);
        clasificadoresService.actualizarUrgencia(id, clasificacionUrgencia);
        return "redirect:/admin/Clasificadores";
    }





    // Desactivar Clasificación CAT
    @GetMapping("/desactivar/catIncidencia/{id}")
    public String desactivarCatIncidencia(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoCatIncidencia(id, false);
        return "redirect:/admin/Clasificadores";
    }

    // Activar Clasificación CAT
    @GetMapping("/activar/catIncidencia/{id}")
    public String activarCatIncidencia(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoCatIncidencia(id, true);
        return "redirect:/admin/Clasificadores";
    }

    // Desactivar Clasificación SUBCAT
    @GetMapping("/desactivar/subCatIncidencia/{id}")
    public String desactivarSubCatIncidencia(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoSubCatIncidencia(id, false);
        return "redirect:/admin/Clasificadores";
    }

    // Activar Clasificación SUBCAT
    @GetMapping("/activar/subCatIncidencia/{id}")
    public String activarSubCatIncidencia(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoSubCatIncidencia(id, true);
        return "redirect:/admin/Clasificadores";
    }

    // Desactivar TIPO Incidencia
    @GetMapping("/desactivar/tipoIncidencia/{id}")
    public String desactivarIncidencia(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoIncidencia(id, false);
        return "redirect:/admin/Clasificadores";
    }

    // Activar TIPO Incidencia
    @GetMapping("/activar/TipoIncidencia/{id}")
    public String activarIncidencia(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoIncidencia(id, true);
        return "redirect:/admin/Clasificadores";
    }

    // Desactivar Clasificación Atencion
    @GetMapping("/desactivar/atencion/{id}")
    public String desactivarAtencion(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoAtencion(id, false);
        return "redirect:/admin/Clasificadores";
    }

    // Activar Clasificación Atencion
    @GetMapping("/activar/atencion/{id}")
    public String activarAtencion(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoAtencion(id, true);
        return "redirect:/admin/Clasificadores";
    }

    // Desactivar Clasificación Urgencia
    @GetMapping("/desactivar/urgencia/{id}")
    public String desactivarUrgencia(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoUrgencia(id, false);
        return "redirect:/admin/Clasificadores";
    }

    // Activar Clasificación Urgencia
    @GetMapping("/activar/urgencia/{id}")
    public String activarUrgencia(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoUrgencia(id, true);
        return "redirect:/admin/Clasificadores";
    }


    // Desactivar Clasificación Desestimacion
    @GetMapping("/desactivar/desestimacion/{id}")
    public String desactivarDesestimacion(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoDesestimacion(id, false);
        return "redirect:/admin/Clasificadores";
    }

    // Activar Clasificación Desestimacion
    @GetMapping("/activar/desestimacion/{id}")
    public String activarDesestimaciona(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoDesestimacion(id, true);
        return "redirect:/admin/Clasificadores";
    }

    // Desactivar Clasificación Area
    @GetMapping("/desactivar/area/{id}")
    public String desactivarArea(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoArea(id, false);
        return "redirect:/admin/Clasificadores";
    }

    // Activar Clasificación Area
    @GetMapping("/activar/area/{id}")
    public String activaAr(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoArea(id, true);
        return "redirect:/admin/Clasificadores";
    }


}
