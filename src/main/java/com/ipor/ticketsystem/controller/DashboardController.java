package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo;
import com.ipor.ticketsystem.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/contador/total")
    public ResponseEntity<Long> getTotalTickets(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        Long ntotal = dashboardService.obtenerNTotalTickets(fechaInicio, fechaFin);
        return ResponseEntity.ok(ntotal);
    }

    @GetMapping("/contador/desestimados")
    public ResponseEntity<Long> getTotalDesestimados(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        Long ntotal = dashboardService.obtenerNTotalDesestimados(fechaInicio, fechaFin);
        return ResponseEntity.ok(ntotal);
    }

    @GetMapping("/contador/recepcionados")
    public ResponseEntity<Long> getTotalRecepcionados(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        Long ntotal = dashboardService.obtenerNTotalRecepcionados(fechaInicio, fechaFin);
        return ResponseEntity.ok(ntotal);
    }

    @GetMapping("/contador/atendidos")
    public ResponseEntity<Long> getTotalAtendidos(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        Long ntotal = dashboardService.obtenerNTotalAtendidos(fechaInicio, fechaFin);
        return ResponseEntity.ok(ntotal);
    }



    //INFO DASHBOARD ADMIN
    @GetMapping("/grafico/TicketsporIncidencia")
    public ResponseEntity<Map<String, Object>> getTxIGraficoData(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<RecordFactorXConteo> conteoTickets = dashboardService.obtenerConteoDeTicketsPorTipoIncidencia(fechaInicio, fechaFin);
        return ResponseEntity.ok(mapearDatosFactorxConteo(conteoTickets));
    }

    @GetMapping("/grafico/TicketsporUrgencia")
    public ResponseEntity<Map<String, Object>> getTxUGraficoData(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<RecordFactorXConteo> conteoTickets = dashboardService.obtenerConteoDeTicketsPorClasificacionUrgencia(fechaInicio, fechaFin);
        return ResponseEntity.ok(mapearDatosFactorxConteo(conteoTickets));
    }

    @GetMapping("/grafico/TicketsporArea")
    public ResponseEntity<Map<String, Object>> getTxAGraficoData(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<RecordFactorXConteo> conteoTickets = dashboardService.obtenerConteoDeTicketsPorArea(fechaInicio, fechaFin);
        return ResponseEntity.ok(mapearDatosFactorxConteo(conteoTickets));
    }



//DAHBOARD SOPORTE
    @GetMapping("/grafico/EstadoActual")
    public ResponseEntity<Map<String, Object>> getGraficoData(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<RecordFactorXConteo> conteoTickets = dashboardService.obtenerConteoDeTicketsPorFase(fechaInicio, fechaFin);
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
    public String obtenerPromedioRecepcion(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        double promedioSegundos = dashboardService.obtenerPromedioSegundosT_R(fechaInicio, fechaFin);
        double promedioMinutos = promedioSegundos / 60;
        return String.format("%.2f", promedioMinutos);
    }

    @GetMapping("/promedioAtencion")
    @ResponseBody
    public String obtenerPromedioAtencion(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        double promedioSegundos = dashboardService.obtenerPromedioSegundosR_S(fechaInicio, fechaFin);
        double promedioMinutos = promedioSegundos / 60;
        return String.format("%.2f", promedioMinutos);
    }

    @GetMapping("/promedioInicioFin")
    @ResponseBody
    public String obtenerPromedioInicioFin(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        double promedioSegundos = dashboardService.obtenerPromedioSegundosT_S(fechaInicio, fechaFin);
        double promedioMinutos = promedioSegundos / 60;
        return String.format("%.2f", promedioMinutos);
    }






}
