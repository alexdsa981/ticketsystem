package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo;
import com.ipor.ticketsystem.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/app/dashboard")
public class DashboardController {
    @Autowired
    DashboardService dashboardService;

    // Endpoint para obtener el número total de tickets
    @GetMapping("/contador/total")
    public ResponseEntity<Long> getTotalTickets() {
        Long ntotal = dashboardService.obtenerNTotalTickets();
        return ResponseEntity.ok(ntotal);
    }

    // Endpoint para obtener el número total de desestimados
    @GetMapping("/contador/desestimados")
    public ResponseEntity<Long> getTotalDesestimados() {
        Long ntotal = dashboardService.obtenerNTotalDesestimados();
        return ResponseEntity.ok(ntotal);
    }

    // Endpoint para obtener el número total de recepcionados
    @GetMapping("/contador/recepcionados")
    public ResponseEntity<Long> getTotalRecepcionados() {
        Long ntotal = dashboardService.obtenerNTotalRecepcionados();
        return ResponseEntity.ok(ntotal);
    }

    // Endpoint para obtener el número total de atendidos
    @GetMapping("/contador/atendidos")
    public ResponseEntity<Long> getTotalAtendidos() {
        Long ntotal = dashboardService.obtenerNTotalAtendidos();
        return ResponseEntity.ok(ntotal);
    }

    // Endpoint para obtener el número total de atendidos
    @GetMapping("/contador/redireccionados")
    public ResponseEntity<Long> getTotalRedireccionados() {
        Long ntotal = dashboardService.obtenerNTotalRedireccionados();
        return ResponseEntity.ok(ntotal);
    }

    @GetMapping("/grafico/EstadoActual")
    public ResponseEntity<Map<String, Object>> getGraficoData(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<RecordFactorXConteo> conteoTickets = dashboardService.obtenerConteoDeTicketsPorFase(fechaInicio, fechaFin);
        return ResponseEntity.ok(mapearDatosFactorxConteo(conteoTickets));
    }

    @GetMapping("/grafico/TicketsporIncidencia")
    public ResponseEntity<Map<String, Object>> getTxIGraficoData(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<RecordFactorXConteo> conteoTickets = dashboardService.obtenerConteoDeTicketsPorClasificacionIncidencia(fechaInicio, fechaFin);
        return ResponseEntity.ok(mapearDatosFactorxConteo(conteoTickets));
    }

    @GetMapping("/grafico/TicketsporUrgencia")
    public ResponseEntity<Map<String, Object>> getTxUGraficoData(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<RecordFactorXConteo> conteoTickets = dashboardService.obtenerConteoDeTicketsPorClasificacionUrgencia(fechaInicio, fechaFin);
        return ResponseEntity.ok(mapearDatosFactorxConteo(conteoTickets));
    }


    private Map<String, Object> mapearDatosFactorxConteo(List<RecordFactorXConteo> lista){
        // Mapear nombres de fase y conteos en listas separadas
        List<String> etiquetas = lista.stream()
                .map(RecordFactorXConteo::nombre)
                .collect(Collectors.toList());
        List<Long> datos = lista.stream()
                .map(ticket -> Optional.ofNullable(ticket.contador()).orElse(0L)) // Si contador() es null, asigna 0L
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("etiquetas", etiquetas);
        response.put("datos", datos);
        return response;
    }

    @GetMapping("/promedioRecepcion")
    @ResponseBody
    public String obtenerPromedioRecepcion() {
        double promedioSegundos = dashboardService.obtenerPromedioSegundosT_R();
        double promedioMinutos = promedioSegundos / 60;  // Convertir a minutos
        return String.format("%.2f", promedioMinutos);  // Formato de 2 decimales
    }

    @GetMapping("/promedioAtencion")
    @ResponseBody
    public String obtenerPromedioAtencion() {
        double promedioSegundos = dashboardService.obtenerPromedioSegundosR_S();
        double promedioMinutos = promedioSegundos / 60;  // Convertir a minutos
        return String.format("%.2f", promedioMinutos);  // Formato de 2 decimales
    }

    @GetMapping("/promedioInicioFin")
    @ResponseBody
    public String obtenerPromedioInicioFin() {
        double promedioSegundos = dashboardService.obtenerPromedioSegundosT_S();
        double promedioMinutos = promedioSegundos / 60;  // Convertir a minutos
        return String.format("%.2f", promedioMinutos);  // Formato de 2 decimales
    }


}
