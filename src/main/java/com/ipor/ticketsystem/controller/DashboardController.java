package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.dto.otros.graficos.RecordConteoTickets_Fase;
import com.ipor.ticketsystem.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/app/dashboard")
public class DashboardController {
    @Autowired
    DashboardService dashboardService;

    @GetMapping("/grafico/EstadoActual")
    public ResponseEntity<Map<String, Object>> getGraficoData() {
        // Obtienes el conteo de tickets por fase
        List<RecordConteoTickets_Fase> conteoTickets = dashboardService.obtenerConteoDeTicketsPorFase();

        // Mapear nombres de fase y conteos en listas separadas
        List<String> etiquetas = conteoTickets.stream()
                .map(RecordConteoTickets_Fase::nombre_fase)
                .collect(Collectors.toList());
        List<Long> datos = conteoTickets.stream()
                .map(RecordConteoTickets_Fase::contador)
                .collect(Collectors.toList());

        // Crear la respuesta en formato JSON
        Map<String, Object> response = new HashMap<>();
        response.put("etiquetas", etiquetas);
        response.put("datos", datos);

        // Devuelve la respuesta como JSON
        return ResponseEntity.ok(response);
    }

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

}
