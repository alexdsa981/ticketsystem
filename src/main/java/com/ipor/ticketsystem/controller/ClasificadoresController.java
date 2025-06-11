package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.fixed.*;
import com.ipor.ticketsystem.repository.fixed.SedeRepository;
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

    public Model listarClasificadores(Model model, String categoriaId, String subcategoriaId, String sedeId) {
        // Cargar listas de Incidencia
        List<CategoriaIncidencia> catIncidencias = clasificadoresService.getListaCatIncidencia();
        model.addAttribute("catIncidencias", catIncidencias);

        if (categoriaId != null && !categoriaId.isEmpty()) {
            List<SubCategoriaIncidencia> subCatIncidencias = clasificadoresService.getListaSubCatIncidenciaPorIDCat(Long.parseLong(categoriaId));
            model.addAttribute("subCatIncidencias", subCatIncidencias);

            if (subcategoriaId != null && !subcategoriaId.isEmpty()) {
                List<TipoIncidencia> tipoIncidencias = clasificadoresService.getListaTipoIncidenciaPirIDSubCat(Long.parseLong(subcategoriaId));
                model.addAttribute("tipoIncidencias", tipoIncidencias);
            }
        } else {
            model.addAttribute("subCatIncidencias", List.of());
            model.addAttribute("tipoIncidencias", List.of());
        }
        if (sedeId != null && !sedeId.isEmpty()) {
            model.addAttribute("sedeSeleccionada", sedeId);
        }
        // Clasificadores comunes
        model.addAttribute("atenciones", clasificadoresService.getListaClasAtencion());
        model.addAttribute("urgencias", clasificadoresService.getListaClasUrgencia());
        model.addAttribute("desestimaciones", clasificadoresService.getListaClasDesestimacion());
        model.addAttribute("esperas", clasificadoresService.getListaClasEspera());

        // Sedes (siempre se cargan para el selector)
        model.addAttribute("sedes", clasificadoresService.getListaSedes());

        // Áreas (solo si se seleccionó una sede)
        if (sedeId != null && !sedeId.isEmpty()) {
            List<AreaAtencion> areas = clasificadoresService.getListaAreaPorSedeId(Long.parseLong(sedeId));
            model.addAttribute("areas", areas);
        } else {
            model.addAttribute("areas", List.of());
        }

        return model;
    }


    //ESPERA ---------------------------------------------------------

    public Model getListaClasificacionesEsperaActivos(Model model){
        List<ClasificacionEspera> listaEspera = clasificadoresService.getListaClasificacionEsperaActivos();
        model.addAttribute("Lista_clasificacion_espera", listaEspera);
        return  model;
    }
    @PostMapping("/espera/nuevo")
    public ResponseEntity<String> crearClasificacionEspera(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        ClasificacionEspera clasificacionEspera = new ClasificacionEspera();
        clasificacionEspera.setNombre(nombre);
        clasificacionEspera.setIsActive(Boolean.TRUE);
        clasificadoresService.saveCEspera(clasificacionEspera);
        response.sendRedirect("/admin/Clasificadores?clasificador=Espera");
        return ResponseEntity.ok("Clasificación Espera creado correctamente");
    }
    @PostMapping("/actualizar/espera/{id}")
    public String actualizarEspera(@PathVariable Long id,
                                          @RequestParam("nombre") String nombre
    ) {
        ClasificacionEspera clasificacionEspera = new ClasificacionEspera();
        clasificacionEspera.setNombre(nombre);
        clasificacionEspera.setIsActive(Boolean.TRUE);
        clasificadoresService.actualizarEspera(id, clasificacionEspera);
        return "redirect:/admin/Clasificadores?clasificador=Espera";
    }
    @GetMapping("/desactivar/espera/{id}")
    public String desactivarEspera(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoEspera(id, false);
        return "redirect:/admin/Clasificadores?clasificador=Espera";
    }
    @GetMapping("/activar/espera/{id}")
    public String activarEsperaa(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoEspera(id, true);
        return "redirect:/admin/Clasificadores?clasificador=Espera";
    }


    //DESESTIMACION ---------------------------------------------------------

    public Model getListaClasificacionesDesestimacionActivos(Model model){
        List<ClasificacionDesestimacion> listaDesestimacion = clasificadoresService.getListaClasificacionDesestimacionActivos();
        model.addAttribute("Lista_clasificacion_desestimacion", listaDesestimacion);
        return  model;
    }
    @PostMapping("/desestimacion/nuevo")
    public ResponseEntity<String> crearClasificacionDesestimacion(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        ClasificacionDesestimacion clasificacionDesestimacion = new ClasificacionDesestimacion();
        clasificacionDesestimacion.setNombre(nombre);
        clasificacionDesestimacion.setIsActive(Boolean.TRUE);
        clasificadoresService.saveCDesestimacion(clasificacionDesestimacion);
        response.sendRedirect("/admin/Clasificadores?clasificador=Desestimacion");
        return ResponseEntity.ok("Clasificación Desestimacion creado correctamente");
    }
    @PostMapping("/actualizar/desestimacion/{id}")
    public String actualizarDesestimacion(@PathVariable Long id,
                                          @RequestParam("nombre") String nombre
    ) {
        ClasificacionDesestimacion clasificacionDesestimacion = new ClasificacionDesestimacion();
        clasificacionDesestimacion.setNombre(nombre);
        clasificacionDesestimacion.setIsActive(Boolean.TRUE);
        clasificadoresService.actualizarDesestimacion(id, clasificacionDesestimacion);
        return "redirect:/admin/Clasificadores?clasificador=Desestimacion";
    }
    @GetMapping("/desactivar/desestimacion/{id}")
    public String desactivarDesestimacion(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoDesestimacion(id, false);
        return "redirect:/admin/Clasificadores?clasificador=Desestimacion";
    }
    @GetMapping("/activar/desestimacion/{id}")
    public String activarDesestimaciona(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoDesestimacion(id, true);
        return "redirect:/admin/Clasificadores?clasificador=Desestimacion";
    }



    //URGENCIAS ---------------------------------------------------------





    public Model getListaClasificacionesUrgenciaActivos(Model model){
        List<ClasificacionUrgencia> listaUrgencias = clasificadoresService.getListaClasificacionUrgenciaActivos();
        model.addAttribute("Lista_clasificacion_urgencia", listaUrgencias);
        return  model;
    }
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

    @GetMapping("/desactivar/urgencia/{id}")
    public String desactivarUrgencia(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoUrgencia(id, false);
        return "redirect:/admin/Clasificadores";
    }
    @GetMapping("/activar/urgencia/{id}")
    public String activarUrgencia(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoUrgencia(id, true);
        return "redirect:/admin/Clasificadores";
    }



    //ATENCION ---------------------------------------------------------





    public Model getListaClasificacionesAtencionActivos(Model model){
        List<ClasificacionAtencion> listaAtencions = clasificadoresService.getListaClasificacionAtencionActivos();
        model.addAttribute("Lista_clasificacion_atencion", listaAtencions);
        return  model;
    }
    @PostMapping("/atencion/nuevo")
    public ResponseEntity<String> crearClasificacionAtencion(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        ClasificacionAtencion clasificacionAtencion = new ClasificacionAtencion();
        clasificacionAtencion.setNombre(nombre);
        clasificacionAtencion.setIsActive(Boolean.TRUE);
        clasificadoresService.saveCAtencion(clasificacionAtencion);
        response.sendRedirect("/admin/Clasificadores?clasificador=Atencion");
        return ResponseEntity.ok("Clasificación Atencion creado correctamente");
    }
    @PostMapping("/actualizar/atencion/{id}")
    public String actualizarAtencion(@PathVariable Long id,
                                     @RequestParam("nombre") String nombre
    ) {
        ClasificacionAtencion clasificacionAtencion = new ClasificacionAtencion();
        clasificacionAtencion.setNombre(nombre);
        clasificacionAtencion.setIsActive(Boolean.TRUE);
        clasificadoresService.actualizarAtencion(id, clasificacionAtencion);
        return "redirect:/admin/Clasificadores?clasificador=Atencion";
    }
    @GetMapping("/desactivar/atencion/{id}")
    public String desactivarAtencion(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoAtencion(id, false);
        return "redirect:/admin/Clasificadores?clasificador=Atencion";
    }
    @GetMapping("/activar/atencion/{id}")
    public String activarAtencion(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoAtencion(id, true);
        return "redirect:/admin/Clasificadores?clasificador=Atencion";
    }








    //AREA ---------------------------------------------------------





    public Model getListaSedesActivos(Model model){
        List<Sede> listaSedes = clasificadoresService.getListaSedesActivos();
        model.addAttribute("Lista_sedes", listaSedes);
        return  model;
    }

    @ResponseBody
    @GetMapping("/area/{sedeId}")
    public List<AreaAtencion> getAreasPorSede(@PathVariable Long sedeId) {
        return clasificadoresService.getListaAreaPorSedeId(sedeId);
    }
    @PostMapping("/sede/nuevo")
    public void crearSede(@RequestParam("nombre") String nombre,
                          @RequestParam(value = "id_sede", required = false) Long id_sede,
                          HttpServletResponse response) throws IOException {
        Sede sede = new Sede();
        sede.setNombre(nombre);
        sede.setIsActive(Boolean.TRUE);
        clasificadoresService.saveSede(sede);
        response.sendRedirect("/admin/Clasificadores?clasificador=Area" + (id_sede != null ? "&sede=" + id_sede : ""));
    }

    @PostMapping("/actualizar/sede/{id}")
    public void actualizarSede(@PathVariable Long id,
                               @RequestParam("nombre") String nombre,
                               @RequestParam(value = "id_sede", required = false) Long id_sede,
                               HttpServletResponse response) throws IOException {
        Sede sede = new Sede();
        sede.setNombre(nombre);
        sede.setIsActive(Boolean.TRUE);
        clasificadoresService.actualizarSede(id, sede);
        response.sendRedirect("/admin/Clasificadores?clasificador=Area" + (id_sede != null ? "&sede=" + id_sede : ""));
    }

    @PostMapping("/area/nuevo")
    public void crearClasificacionAr(@RequestParam("id_sede") Long id_sede,
                                     @RequestParam("nombre") String nombre,
                                     HttpServletResponse response) throws IOException {
        AreaAtencion areaAtencion = new AreaAtencion();
        areaAtencion.setNombre(nombre);
        areaAtencion.setIsActive(Boolean.TRUE);
        Sede sede = clasificadoresService.getSedePorId(id_sede);
        areaAtencion.setSede(sede);
        clasificadoresService.saveCArea(areaAtencion);
        response.sendRedirect("/admin/Clasificadores?clasificador=Area" + (id_sede != null ? "&sede=" + id_sede : ""));
    }

    @PostMapping("/actualizar/area/{id}")
    public void actualizarArea(@PathVariable Long id,
                               @RequestParam("id_sede") Long id_sede,
                               @RequestParam("id_sedeEdit") Long id_sedeEdit,
                               @RequestParam("nombre") String nombre,
                               HttpServletResponse response) throws IOException {

        Sede sede = clasificadoresService.getSedePorId(id_sedeEdit);
        AreaAtencion areaAtencion = new AreaAtencion();
        areaAtencion.setNombre(nombre);
        areaAtencion.setIsActive(Boolean.TRUE);
        areaAtencion.setSede(sede);
        clasificadoresService.actualizarArea(id, areaAtencion);
        response.sendRedirect("/admin/Clasificadores?clasificador=Area" + (id_sede != null ? "&sede=" + id_sede : ""));
    }
    @GetMapping("/desactivar/area/{id}")
    public ResponseEntity<String> desactivarArea(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoArea(id, false);
        return ResponseEntity.ok("Área desactivada correctamente");
    }
    @GetMapping("/activar/area/{id}")
    public ResponseEntity<String> activarArea(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoArea(id, true);
        return ResponseEntity.ok("Área activada correctamente");
    }
    @GetMapping("/desactivar/sede/{id}")
    public ResponseEntity<String> desactivarSede(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoSede(id, false);
        return ResponseEntity.ok("Sede desactivada correctamente");
    }
    @GetMapping("/activar/sede/{id}")
    public ResponseEntity<String> activarSede(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoSede(id, true);
        return ResponseEntity.ok("Sede activada correctamente");
    }
    @ResponseBody
    @GetMapping("/listar/areasActivas/{id_sede}")
    public List<AreaAtencion> listaAreasActivos(@PathVariable Long id_sede) {
        return clasificadoresService.getListaAreasActivosPorIDSede(id_sede);
    }




    //INCIDENCIAS ---------------------------------------------------------





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
    @ResponseBody
    @GetMapping("/subcategorias/{categoriaId}")
    public List<SubCategoriaIncidencia> getSubcategoriasPorCategoria(@PathVariable Long categoriaId) {
        return clasificadoresService.getListaSubCatIncidenciaPorIDCat(categoriaId);
    }
    @ResponseBody
    @GetMapping("/tipos/{subCategoriaId}")
    public List<TipoIncidencia> getTiposPorSubCategoria(@PathVariable Long subCategoriaId) {
        return clasificadoresService.getListaTipoIncidenciaPirIDSubCat(subCategoriaId);
    }
    @PostMapping("/catIncidencia/nuevo")
    public ResponseEntity<String> crearTipoIncidenciaa(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        CategoriaIncidencia categoriaIncidencia = new CategoriaIncidencia();
        categoriaIncidencia.setNombre(nombre);
        categoriaIncidencia.setIsActive(Boolean.TRUE);
        clasificadoresService.saveCatIncidencia(categoriaIncidencia);
        return ResponseEntity.ok("Clasificación Incidencia creado correctamente");
    }
    @PostMapping("/actualizar/catIncidencia/{id}")
    public ResponseEntity<String>  actualizarIncidencia(@PathVariable Long id,
                                                        @RequestParam("nombre") String nombre
    ) {
        CategoriaIncidencia categoriaIncidencia = new CategoriaIncidencia();
        categoriaIncidencia.setNombre(nombre);
        categoriaIncidencia.setIsActive(Boolean.TRUE);
        clasificadoresService.actualizarCatIncidencia(id, categoriaIncidencia);
        return ResponseEntity.ok("Clasificación Incidencia actualizado correctamente");
    }
    @PostMapping("/subCatIncidencia/nuevo")
    public ResponseEntity<String> crearSubCatIncidenciaa(
            @RequestParam("id_cat") Long id_cat,
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        SubCategoriaIncidencia sbCategoriaIncidencia = new SubCategoriaIncidencia();
        sbCategoriaIncidencia.setNombre(nombre);
        sbCategoriaIncidencia.setCategoriaIncidencia(clasificadoresService.getCatIncidenciaPorID(id_cat));
        sbCategoriaIncidencia.setIsActive(Boolean.TRUE);
        clasificadoresService.saveSubCatIncidencia(sbCategoriaIncidencia);
        response.sendRedirect("/admin/Clasificadores?clasificador=Incidencia");
        return ResponseEntity.ok("Clasificación sbCategoriaIncidencia creado correctamente");
    }
    @PostMapping("/actualizar/subCatIncidencia/{id}")
    public ResponseEntity<String> actualizarIncidencia(@PathVariable Long id,
                                                       @RequestParam("id_cat") Long id_cat,
                                                       @RequestParam("nombre") String nombre) {
        SubCategoriaIncidencia sbCategoriaIncidencia = new SubCategoriaIncidencia();
        sbCategoriaIncidencia.setNombre(nombre);
        sbCategoriaIncidencia.setCategoriaIncidencia(clasificadoresService.getCatIncidenciaPorID(id_cat));
        sbCategoriaIncidencia.setIsActive(Boolean.TRUE);
        clasificadoresService.actualizarSubCatIncidencia(id, sbCategoriaIncidencia);
        return ResponseEntity.ok("Clasificación sbCategoriaIncidencia actualizado correctamente");
    }
    @PostMapping("/tipoIncidencia/nuevo")
    public ResponseEntity<String> crearTipoIncidenciaa(
            @RequestParam("nombre") String nombre,
            @RequestParam("id_subcat") Long id_subcat,
            HttpServletResponse response) throws IOException {
        TipoIncidencia tipoIncidencia = new TipoIncidencia();
        tipoIncidencia.setNombre(nombre);
        tipoIncidencia.setSubCategoriaIncidencia(clasificadoresService.getSubCatIncidenciaPorID(id_subcat));
        tipoIncidencia.setIsActive(Boolean.TRUE);
        clasificadoresService.saveTIncidencia(tipoIncidencia);
        response.sendRedirect("/admin/Clasificadores?clasificador=Incidencia");
        return ResponseEntity.ok("Clasificación Incidencia creado correctamente");
    }
    @PostMapping("/actualizar/tipoIncidencia/{id}")
    public ResponseEntity<String>  actualizarTipoIncidencia(@PathVariable Long id,
                                                            @RequestParam("id_subcat") Long id_subcat,
                                                            @RequestParam("nombre") String nombre) {
        TipoIncidencia tipoIncidencia = new TipoIncidencia();
        tipoIncidencia.setNombre(nombre);
        tipoIncidencia.setSubCategoriaIncidencia(clasificadoresService.getSubCatIncidenciaPorID(id_subcat));
        tipoIncidencia.setIsActive(Boolean.TRUE);
        clasificadoresService.actualizarTipoIncidencia(id, tipoIncidencia);
        return ResponseEntity.ok("Clasificación Incidencia actualizado correctamente");
    }
    @GetMapping("/desactivar/catIncidencia/{id}")
    public ResponseEntity<String>  desactivarCatIncidencia(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoCatIncidencia(id, false);
        return ResponseEntity.ok("Clasificación Incidencia actualizado correctamente");
    }
    @GetMapping("/activar/catIncidencia/{id}")
    public ResponseEntity<String>  activarCatIncidencia(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoCatIncidencia(id, true);
        return ResponseEntity.ok("Clasificación Incidencia actualizado correctamente");
    }
    @GetMapping("/desactivar/subCatIncidencia/{id}")
    public ResponseEntity<String>  desactivarSubCatIncidencia(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoSubCatIncidencia(id, false);
        return ResponseEntity.ok("sub categoria desactivado actualizado correctamente");
    }
    @GetMapping("/activar/subCatIncidencia/{id}")
    public ResponseEntity<String>  activarSubCatIncidencia(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoSubCatIncidencia(id, true);
        return ResponseEntity.ok("sub categoria activado correctamente");
    }
    @GetMapping("/desactivar/tipoIncidencia/{id}")
    public ResponseEntity<String>  desactivarIncidencia(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoIncidencia(id, false);
        return ResponseEntity.ok("tipo Incidencia desactivado actualizado correctamente");
    }
    @GetMapping("/activar/tipoIncidencia/{id}")
    public ResponseEntity<String>  activarIncidencia(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoIncidencia(id, true);
        return ResponseEntity.ok("tipo Incidencia activado correctamente");
    }
    @ResponseBody
    @GetMapping("/listar/subCatIncidenciaActivos/{id_categoria}")
    public List<SubCategoriaIncidencia> listaSubCategoriasIncidenciaActivos(@PathVariable Long id_categoria) {
        return clasificadoresService.getListaSubCatIncidenciaActivosPorIDCat(id_categoria);
    }
    @ResponseBody
    @GetMapping("/listar/tipoIncidenciaActivos/{id_subCategoria}")
    public List<TipoIncidencia> listaTipoIncidenciaActivos(@PathVariable Long id_subCategoria) {
        return clasificadoresService.getListaTiposDeIncidenciaActivosPorIDSubCat(id_subCategoria);
    }

}
