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


    // Listar todos los clasificadores (Incidencias, Servicios, Urgencias)

    public Model listarClasificadores(Model model) {
        List<ClasificacionIncidencia> incidencias = clasificadoresService.getListaClasIncidencia();
        List<ClasificacionServicio> servicios = clasificadoresService.getListaClasServicio();
        List<ClasificacionUrgencia> urgencias = clasificadoresService.getListaClasUrgencia();
        List<ClasificacionDesestimacion> desestimaciones = clasificadoresService.getListaClasDesestimacion();
        List<TipoComponente> tipoComponentes = clasificadoresService.getListaTipoComponente();

        model.addAttribute("tipoComponentes", tipoComponentes);
        model.addAttribute("desestimaciones", desestimaciones);
        model.addAttribute("incidencias", incidencias);
        model.addAttribute("servicios", servicios);
        model.addAttribute("urgencias", urgencias);
        return model;
    }

    public Model getListaClasificacionIncidenciaActivos(Model model) {
        List<ClasificacionIncidencia> ListaTiposIncidencia = clasificadoresService.getListaTiposDeIncidenciaActivos();
        model.addAttribute("Lista_clasificacion_incidencia", ListaTiposIncidencia);
        return model;
    }

    public Model getListaTipoComponentesActivos(Model model) {
        List<TipoComponente> ListaTiposComponentes = clasificadoresService.getListaTiposDeComponentesActivos();
        model.addAttribute("Lista_tipo_componentes", ListaTiposComponentes);
        return model;
    }
    public Model getListaClasificacionesUrgenciaActivos(Model model){
        List<ClasificacionUrgencia> listaUrgencias = clasificadoresService.getListaClasificacionUrgenciaActivos();
        model.addAttribute("Lista_clasificacion_urgencia", listaUrgencias);
        return  model;
    }
    public Model getListaClasificacionesServicioActivos(Model model){
        List<ClasificacionServicio> listaServicios = clasificadoresService.getListaClasificacionServicioActivos();
        model.addAttribute("Lista_clasificacion_servicio", listaServicios);
        return  model;
    }
    public Model getListaClasificacionesDesestimacionActivos(Model model){
        List<ClasificacionDesestimacion> listaDesestimacion = clasificadoresService.getListaClasificacionDesestimacionActivos();
        model.addAttribute("Lista_clasificacion_desestimacion", listaDesestimacion);
        return  model;
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
    // Actualizar un incidencia existente
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
    @PostMapping("/incidencia/nuevo")
    public ResponseEntity<String> crearClasificacionIncidencia(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        ClasificacionIncidencia clasificacionIncidencia = new ClasificacionIncidencia();
        clasificacionIncidencia.setNombre(nombre);
        clasificacionIncidencia.setIsActive(Boolean.TRUE);
        clasificadoresService.saveCIncidencia(clasificacionIncidencia);
        response.sendRedirect("/admin/Clasificadores");
        return ResponseEntity.ok("Clasificación Incidencia creado correctamente");
    }

    // Actualizar un incidencia existente
    @PostMapping("/actualizar/incidencia/{id}")
    public String actualizarIncidencia(@PathVariable Long id,
                                    @RequestParam("nombre") String nombre
    ) {
        ClasificacionIncidencia clasificacionIncidencia = new ClasificacionIncidencia();
        clasificacionIncidencia.setNombre(nombre);
        clasificacionIncidencia.setIsActive(Boolean.TRUE);
        clasificadoresService.actualizarIncidencia(id, clasificacionIncidencia);
        return "redirect:/admin/Clasificadores";
    }

    //crear Clasificacion servicio nueva
    @PostMapping("/servicio/nuevo")
    public ResponseEntity<String> crearClasificacionServicio(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        ClasificacionServicio clasificacionServicio = new ClasificacionServicio();
        clasificacionServicio.setNombre(nombre);
        clasificacionServicio.setIsActive(Boolean.TRUE);
        clasificadoresService.saveCServicio(clasificacionServicio);
        response.sendRedirect("/admin/Clasificadores");
        return ResponseEntity.ok("Clasificación Servicio creado correctamente");
    }

    // Actualizar un servicio existente
    @PostMapping("/actualizar/servicio/{id}")
    public String actualizarServicio(@PathVariable Long id,
                                       @RequestParam("nombre") String nombre
    ) {
        ClasificacionServicio clasificacionServicio = new ClasificacionServicio();
        clasificacionServicio.setNombre(nombre);
        clasificacionServicio.setIsActive(Boolean.TRUE);
        clasificadoresService.actualizarServicio(id, clasificacionServicio);
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

    //crear Tipo Componente nuevo
    @PostMapping("/tipoComponente/nuevo")
    public ResponseEntity<String> crearTipoComponente(
            @RequestParam("nombre") String nombre,
            HttpServletResponse response) throws IOException {
        TipoComponente tipoComponente = new TipoComponente();
        tipoComponente.setNombre(nombre);
        tipoComponente.setIsActive(Boolean.TRUE);
        clasificadoresService.saveTipoComponente(tipoComponente);
        response.sendRedirect("/admin/Clasificadores");
        return ResponseEntity.ok("Tipo Componente creado correctamente");
    }

    // Actualizar un Urgencia existente
    @PostMapping("/actualizar/tipoComponente/{id}")
    public String actualizarTipoComponente(@PathVariable Long id,
                                     @RequestParam("nombre") String nombre
    ) {
        TipoComponente tipoComponente = new TipoComponente();
        tipoComponente.setNombre(nombre);
        tipoComponente.setIsActive(Boolean.TRUE);
        clasificadoresService.actualizarTipoComponente(id, tipoComponente);
        return "redirect:/admin/Clasificadores";
    }


    // Desactivar Clasificación Incidencia
    @GetMapping("/desactivar/incidencia/{id}")
    public String desactivarIncidencia(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoIncidencia(id, false);
        return "redirect:/admin/Clasificadores";
    }

    // Activar Clasificación Incidencia
    @GetMapping("/activar/incidencia/{id}")
    public String activarIncidencia(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoIncidencia(id, true);
        return "redirect:/admin/Clasificadores";
    }

    // Desactivar Clasificación Servicio
    @GetMapping("/desactivar/servicio/{id}")
    public String desactivarServicio(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoServicio(id, false);
        return "redirect:/admin/Clasificadores";
    }

    // Activar Clasificación Servicio
    @GetMapping("/activar/servicio/{id}")
    public String activarServicio(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoServicio(id, true);
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

    // Desactivar Tipo Componente
    @GetMapping("/desactivar/tipoComponente/{id}")
    public String desactivarTipoComponente(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoTipoComponente(id, false);
        return "redirect:/admin/Clasificadores";
    }

    // Activar Clasificación Desestimacion
    @GetMapping("/activar/tipoComponente/{id}")
    public String activarTipoComponente(@PathVariable Long id) {
        clasificadoresService.cambiarEstadoTipoComponente(id, true);
        return "redirect:/admin/Clasificadores";
    }


}