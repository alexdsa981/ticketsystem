package com.ipor.ticketsystem.dashboard.soporte;

import com.ipor.ticketsystem.core.websocket.dto.TicketRecordWS;
import com.ipor.ticketsystem.dashboard.RecordFactorXConteo;
import com.ipor.ticketsystem.ticket.Ticket;
import com.ipor.ticketsystem.ticket.dto.DetalleTicketDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/app/dashboard")
public class DashboardSoporteController {
    @Autowired
    DashboardSoporteService dashboardSoporteService;


    //DAHBOARD SOPORTE
    @GetMapping("/grafico/EstadoActual")
    public ResponseEntity<Map<String, Object>> getGraficoData(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<RecordFactorXConteo> conteoTickets = dashboardSoporteService.obtenerConteoDeTicketsPorFase(fechaInicio, fechaFin);
        return ResponseEntity.ok(mapearDatosFactorxConteo(conteoTickets));
    }


    @GetMapping("/tickets/recientes")
    public ResponseEntity<Map<String, Object>> getTicketsRecientes() {
        List<Ticket> tickets = dashboardSoporteService.Obtener5TicketsMasRecientes();
        List<TicketRecordWS> listaWS = new ArrayList<>();
        for (Ticket ticket : tickets){
            DetalleTicketDTO detalleTicketDTO = new DetalleTicketDTO(ticket);
            TicketRecordWS ticketRecordWS = new TicketRecordWS(detalleTicketDTO);
            listaWS.add(ticketRecordWS);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("tickets", listaWS);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/tickets/inactivos")
    public ResponseEntity<Map<String, Object>> obtenerTicketsInactivos() {
        List<TicketInactivoProjection> tickets = dashboardSoporteService.obtenerTicketsInactivosMasAntiguos();
        Map<String, Object> response = new HashMap<>();
        response.put("tickets", tickets);
        return ResponseEntity.ok(response);
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




}
