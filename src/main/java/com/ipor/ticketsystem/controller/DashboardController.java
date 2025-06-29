package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.dto.DetalleTicketDTO;
import com.ipor.ticketsystem.model.dto.otros.TicketInactivoProjection;
import com.ipor.ticketsystem.model.dto.otros.WebSocket.TicketRecordWS;
import com.ipor.ticketsystem.model.dto.otros.graficos.RecordFactorXConteo;
import com.ipor.ticketsystem.model.dynamic.Notificacion;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.service.DashboardService;
import com.ipor.ticketsystem.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/app/dashboard")
public class DashboardController {
    @Autowired
    DashboardService dashboardService;
    @Autowired
    UsuarioService usuarioService;

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
    @GetMapping("/grafico/TicketsporCategoria")
    public ResponseEntity<Map<String, Object>> getTxCatGraficoData(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<RecordFactorXConteo> conteoTickets = dashboardService.obtenerConteoDeTicketsPorCategoriaIncidencia(fechaInicio, fechaFin);
        return ResponseEntity.ok(mapearDatosFactorxConteo(conteoTickets));
    }


    @GetMapping("/grafico/TicketsporSubcategoria")
    public ResponseEntity<Map<String, Object>> getTxSubcatGraficoData(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<RecordFactorXConteo> conteoTickets = dashboardService.obtenerConteoDeTicketsPorSubcategoriaIncidencia(fechaInicio, fechaFin);
        return ResponseEntity.ok(mapearDatosFactorxConteo(conteoTickets));
    }

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

    @GetMapping("/grafico/TicketsporSede")
    public ResponseEntity<Map<String, Object>> getTxSGraficoData(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<RecordFactorXConteo> conteoTickets = dashboardService.obtenerConteoDeTicketsPorSede(fechaInicio, fechaFin);
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

    @GetMapping("/tickets/recientes")
    public ResponseEntity<Map<String, Object>> getTicketsRecientes() {
        List<Ticket> tickets = dashboardService.Obtener5TicketsMasRecientes();
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
        List<TicketInactivoProjection> tickets = dashboardService.obtenerTicketsInactivosMasAntiguos();
        Map<String, Object> response = new HashMap<>();
        response.put("tickets", tickets);
        return ResponseEntity.ok(response);
    }








    @GetMapping("/promedioRecepcion")
    @ResponseBody
    public String obtenerPromedioRecepcion(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        double promedioSegundos = dashboardService.obtenerPromedioSegundosTicket_Recepcion(fechaInicio, fechaFin);
        double promedioMinutos = promedioSegundos / 60;
        return String.format("%.2f", promedioMinutos);
    }

    @GetMapping("/promedioAtencion")
    @ResponseBody
    public String obtenerPromedioAtencion(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        double promedioSegundos = dashboardService.obtenerPromedioSegundosRecepcion_Atencion(fechaInicio, fechaFin);
        double promedioMinutos = promedioSegundos / 60;
        return String.format("%.2f", promedioMinutos);
    }

    @GetMapping("/promedioInicioFin")
    @ResponseBody
    public String obtenerPromedioInicioFin(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        double promedioSegundos = dashboardService.obtenerPromedioSegundosTicket_Atencion(fechaInicio, fechaFin);
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
            Map<String, Object> datos = dashboardService.obtenerTiempoEfectivoYCantidadTicketsPorUsuario(soporte.getId(), fechaInicio, fechaFin);

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
            Map<String, Object> datos = dashboardService.obtenerTiempoEfectivoYCantidadTicketsPorUsuario(admin.getId(), fechaInicio, fechaFin);

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
