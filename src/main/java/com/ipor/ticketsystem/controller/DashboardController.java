package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.dto.otros.graficos.RecordConteoTicketxFactor;
import com.ipor.ticketsystem.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/grafico/EstadoActual")
    public ResponseEntity<Map<String, Object>> getGraficoData() {
        // Obtienes el conteo de tickets por fase
        List<RecordConteoTicketxFactor> conteoTickets = dashboardService.obtenerConteoDeTicketsPorFase();

        // Mapear nombres de fase y conteos en listas separadas
        List<String> etiquetas = conteoTickets.stream()
                .map(RecordConteoTicketxFactor::nombre)
                .collect(Collectors.toList());
        List<Long> datos = conteoTickets.stream()
                .map(ticket -> Optional.ofNullable(ticket.contador()).orElse(0L)) // Si contador() es null, asigna 0L
                .collect(Collectors.toList());

        // Crear la respuesta en formato JSON
        Map<String, Object> response = new HashMap<>();
        response.put("etiquetas", etiquetas);
        response.put("datos", datos);

        // Devuelve la respuesta como JSON
        return ResponseEntity.ok(response);
    }

    @GetMapping("/grafico/TicketsporIncidencia")
    public ResponseEntity<Map<String, Object>> getTxIGraficoData() {
        // Obtienes el conteo de tickets por fase
        List<RecordConteoTicketxFactor> conteoTickets = dashboardService.obtenerConteoDeTicketsPorClasificacionIncidencia();

        // Mapear nombres de fase y conteos en listas separadas
        List<String> etiquetas = conteoTickets.stream()
                .map(RecordConteoTicketxFactor::nombre)
                .collect(Collectors.toList());
        List<Long> datos = conteoTickets.stream()
                .map(ticket -> Optional.ofNullable(ticket.contador()).orElse(0L)) // Si contador() es null, asigna 0L
                .collect(Collectors.toList());

        // Crear la respuesta en formato JSON
        Map<String, Object> response = new HashMap<>();
        response.put("etiquetas", etiquetas);
        response.put("datos", datos);

        // Devuelve la respuesta como JSON
        return ResponseEntity.ok(response);
    }

    @GetMapping("/grafico/TicketsporUrgencia")
    public ResponseEntity<Map<String, Object>> getTxUGraficoData() {
        // Obtienes el conteo de tickets por fase
        List<RecordConteoTicketxFactor> conteoTickets = dashboardService.obtenerConteoDeTicketsPorClasificacionUrgencia();

        // Mapear nombres de fase y conteos en listas separadas
        List<String> etiquetas = conteoTickets.stream()
                .map(RecordConteoTicketxFactor::nombre)
                .collect(Collectors.toList());
        List<Long> datos = conteoTickets.stream()
                .map(ticket -> Optional.ofNullable(ticket.contador()).orElse(0L)) // Si contador() es null, asigna 0L
                .collect(Collectors.toList());

        // Crear la respuesta en formato JSON
        Map<String, Object> response = new HashMap<>();
        response.put("etiquetas", etiquetas);
        response.put("datos", datos);

        // Devuelve la respuesta como JSON
        return ResponseEntity.ok(response);
    }

}
