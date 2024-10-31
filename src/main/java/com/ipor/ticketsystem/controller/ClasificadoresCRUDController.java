package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.model.fixed.ClasificacionIncidencia;
import com.ipor.ticketsystem.model.fixed.ClasificacionServicio;
import com.ipor.ticketsystem.model.fixed.ClasificacionUrgencia;
import com.ipor.ticketsystem.repository.fixed.ClasificacionIncidenciaRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionServicioRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionUrgenciaRepository;
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
public class ClasificadoresCRUDController {
    @Autowired
    ClasificadoresService clasificadoresService;


    // Listar todos los clasificadores (Incidencias, Servicios, Urgencias)

    public Model listarClasificadores(Model model) {
        List<ClasificacionIncidencia> incidencias = clasificadoresService.RetornaListaClasIncidencia();
        List<ClasificacionServicio> servicios = clasificadoresService.RetornaListaClasServicio();
        List<ClasificacionUrgencia> urgencias = clasificadoresService.RetornaListaClasUrgencia();

        model.addAttribute("incidencias", incidencias);
        model.addAttribute("servicios", servicios);
        model.addAttribute("urgencias", urgencias);
        return model;
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






}
