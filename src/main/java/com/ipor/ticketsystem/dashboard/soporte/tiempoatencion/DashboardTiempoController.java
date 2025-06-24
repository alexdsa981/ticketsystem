package com.ipor.ticketsystem.dashboard.soporte.tiempoatencion;

import com.ipor.ticketsystem.usuario.Usuario;
import com.ipor.ticketsystem.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/app/dashboard")
public class DashboardTiempoController {
    @Autowired
    DashboardTiempoService dashboardTiempoService;
    @Autowired
    UsuarioService usuarioService;



    @GetMapping("/promedioRecepcion")
    @ResponseBody
    public String obtenerPromedioRecepcion(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        double promedioSegundos = dashboardTiempoService.obtenerPromedioSegundosTicket_Recepcion(fechaInicio, fechaFin);
        double promedioMinutos = promedioSegundos / 60;
        return String.format("%.2f", promedioMinutos);
    }

    @GetMapping("/promedioAtencion")
    @ResponseBody
    public String obtenerPromedioAtencion(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        double promedioSegundos = dashboardTiempoService.obtenerPromedioSegundosRecepcion_Atencion(fechaInicio, fechaFin);
        double promedioMinutos = promedioSegundos / 60;
        return String.format("%.2f", promedioMinutos);
    }

    @GetMapping("/promedioInicioFin")
    @ResponseBody
    public String obtenerPromedioInicioFin(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        double promedioSegundos = dashboardTiempoService.obtenerPromedioSegundosTicket_Atencion(fechaInicio, fechaFin);
        double promedioMinutos = promedioSegundos / 60;
        return String.format("%.2f", promedioMinutos);
    }




    @GetMapping("/tiempo-efectivo-usuario")
    @ResponseBody
    public List<Map<String, Object>> obtenerTiempoEfectivoPorUsuario(
            @RequestParam(value = "fechaInicio", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<Map<String, Object>> resultado = new ArrayList<>();

        // Soportes (rol 2)
        List<Usuario> listaSoportes = usuarioService.ListaUsuariosPorRol(2L);
        for (Usuario soporte : listaSoportes) {
            Map<String, Object> datos = dashboardTiempoService.obtenerTiempoEfectivoYCantidadTicketsPorUsuario(soporte.getId(), fechaInicio, fechaFin);

            Map<String, Object> soporteInfo = Map.of(
                    "usuario", soporte.getNombre(),
                    "rol", "Soporte",
                    "minutosEfectivos", String.format("%.2f", (Double) datos.get("promedioMinutos")),
                    "cantidadTickets", datos.get("cantidadTickets")
            );
            resultado.add(soporteInfo);
        }

        // Admins (rol 3)
        List<Usuario> listaAdmins = usuarioService.ListaUsuariosPorRol(3L);
        for (Usuario admin : listaAdmins) {
            Map<String, Object> datos = dashboardTiempoService.obtenerTiempoEfectivoYCantidadTicketsPorUsuario(admin.getId(), fechaInicio, fechaFin);

            Map<String, Object> adminInfo = Map.of(
                    "usuario", admin.getNombre(),
                    "rol", "Admin",
                    "minutosEfectivos", String.format("%.2f", (Double) datos.get("promedioMinutos")),
                    "cantidadTickets", datos.get("cantidadTickets")
            );
            resultado.add(adminInfo);
        }

        return resultado;
    }

}