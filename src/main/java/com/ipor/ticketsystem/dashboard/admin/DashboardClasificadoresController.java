package com.ipor.ticketsystem.dashboard.admin;

import com.ipor.ticketsystem.dashboard.RecordFactorXConteo;
import com.ipor.ticketsystem.dashboard.admin.exportar.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/app/dashboard")
public class DashboardClasificadoresController {
    @Autowired
    DashboardClasificadoresService dashboardClasificadoresService;
    @Autowired
    DashboardService dashboardService;
    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping("/grafico/tickets")
    public ResponseEntity<Map<String, Object>> obtenerGraficoTickets(
            @RequestParam String agruparPor,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Long idSede,
            @RequestParam(required = false) Long idArea,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) Long idSubcategoria,
            @RequestParam(required = false) Long idTipoIncidencia,
            @RequestParam(required = false) Long idTipoUrgencia,
            @RequestParam(required = false) Long idUsuario
    ) {
        List<RecordFactorXConteo> datos = dashboardService.obtenerConteoDinamico(
                agruparPor, fechaInicio, fechaFin,
                idSede, idArea, idCategoria, idSubcategoria,
                idTipoIncidencia, idTipoUrgencia, idUsuario
        );

        Map<String, Object> data = mapearDatosFactorxConteo(datos);

        // Aquí agregas los indicadores
        IndicadorResolucionDTO indicador = dashboardService.obtenerIndicadoresResolucion(
                fechaInicio, fechaFin,
                idSede, idArea, idCategoria, idSubcategoria,
                idTipoIncidencia, idTipoUrgencia, idUsuario
        );

        data.put("porcentajeResueltos", indicador.getPorcentajeResueltos());
        data.put("porcentajeNoResueltos", indicador.getPorcentajeNoResueltos());
        data.put("total", indicador.getTotal());
        data.put("resueltos", indicador.getResueltos());

        data.put("porcentajeErrorUsuario", indicador.getPorcentajeErrorUsuario());
        data.put("errorUsuario", indicador.getErrorUsuario());

        return ResponseEntity.ok(data);
    }

    @GetMapping("/exporta/tickets/filtrados")
    public ResponseEntity<byte[]> exportarTicketsFiltrados(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Long idSede,
            @RequestParam(required = false) Long idArea,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(required = false) Long idSubcategoria,
            @RequestParam(required = false) Long idTipoIncidencia,
            @RequestParam(required = false) Long idTipoUrgencia,
            @RequestParam(required = false) Long idUsuario
    ) {
        List<Long> idsFiltrados = dashboardService.obtenerIdsTicketsFiltrados(
                fechaInicio, fechaFin,
                idSede, idArea, idCategoria, idSubcategoria,
                idTipoIncidencia, idTipoUrgencia, idUsuario
        );

        try {
            ByteArrayOutputStream outputStream = excelExportService.exportToExcel(idsFiltrados);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=tickets_filtrados.xlsx");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @GetMapping("/contador/total")
    public ResponseEntity<Long> getTotalTickets(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        Long ntotal = dashboardClasificadoresService.obtenerNTotalTickets(fechaInicio, fechaFin);
        return ResponseEntity.ok(ntotal);
    }

    @GetMapping("/contador/desestimados")
    public ResponseEntity<Long> getTotalDesestimados(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        Long ntotal = dashboardClasificadoresService.obtenerNTotalDesestimados(fechaInicio, fechaFin);
        return ResponseEntity.ok(ntotal);
    }

    @GetMapping("/contador/recepcionados")
    public ResponseEntity<Long> getTotalRecepcionados(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        Long ntotal = dashboardClasificadoresService.obtenerNTotalRecepcionados(fechaInicio, fechaFin);
        return ResponseEntity.ok(ntotal);
    }

    @GetMapping("/contador/atendidos")
    public ResponseEntity<Long> getTotalAtendidos(
            @RequestParam(value = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(value = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        Long ntotal = dashboardClasificadoresService.obtenerNTotalAtendidos(fechaInicio, fechaFin);
        return ResponseEntity.ok(ntotal);
    }




    private Map<String, Object> mapearDatosFactorxConteo(List<RecordFactorXConteo> lista) {
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