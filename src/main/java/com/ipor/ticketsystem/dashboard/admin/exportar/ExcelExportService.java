package com.ipor.ticketsystem.dashboard.admin.exportar;

import com.ipor.ticketsystem.ticket.Ticket;
import com.ipor.ticketsystem.ticket.dto.DetalleTicketDTO;
import com.ipor.ticketsystem.ticket.repository.TicketRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExcelExportService {
    private final TicketRepository ticketRepository;

    public ExcelExportService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public ByteArrayOutputStream exportToExcel(List<Long> idsFiltrados) throws IOException {
        List<Ticket> tickets = ticketRepository.findByIdIn(idsFiltrados);
        List<TicketExportDTO> datos = tickets.stream()
                .map(DetalleTicketDTO::new)
                .map(TicketExportDTO::convertirADTOExportacion)
                .collect(Collectors.toList());

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Tickets");

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(font);

        String[] headers = {
                "ID", "Código", "Fecha Ticket", "Hora Ticket", "Fecha Recepción", "Hora Recepción",
                "Fecha Atención", "Hora Atención",

                "Fecha Inicio Espera 1", "Hora Inicio Espera 1", "Fecha Fin Espera 1", "Hora Fin Espera 1",
                "Fecha Inicio Espera 2", "Hora Inicio Espera 2", "Fecha Fin Espera 2", "Hora Fin Espera 2",

                "Usuario Ticket", "Usuario Recepción", "Usuario Atención",
                "Usuario Espera 1", "Usuario Espera 2",

                "Descripción Ticket", "Mensaje Recepción", "Descripción Atención",
                "Descripción Espera 1", "Descripción Espera 2",

                "Fase", "Categoria", "Subcategoría", "Tipo Incidencia", "Urgencia", "Clasificación Atención",
                "Área Atención", "Sede Atención",
                "Clasificación Espera 1", "Clasificación Espera 2",

                "Total Espera (s)", "Tiempo Total Ticket-Atención (s)", "Tiempo Útil de Atención (s)"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        int rowNum = 1;

        for (TicketExportDTO dto : datos) {
            Row row = sheet.createRow(rowNum++);
            int col = 0;

            row.createCell(col++).setCellValue(dto.getIdTicket());
            row.createCell(col++).setCellValue(dto.getIdFormateadoTicket());
            row.createCell(col++).setCellValue(dto.getFechaFormateadaTicket());
            row.createCell(col++).setCellValue(dto.getHoraFormateadaTicket());
            row.createCell(col++).setCellValue(dto.getFechaFormateadaRecepcion());
            row.createCell(col++).setCellValue(dto.getHoraFormateadaRecepcion());
            row.createCell(col++).setCellValue(dto.getFechaFormateadaAtencion());
            row.createCell(col++).setCellValue(dto.getHoraFormateadaAtencion());

            row.createCell(col++).setCellValue(dto.getFechaInicioEspera1());
            row.createCell(col++).setCellValue(dto.getHoraInicioEspera1());
            row.createCell(col++).setCellValue(dto.getFechaFinEspera1());
            row.createCell(col++).setCellValue(dto.getHoraFinEspera1());
            row.createCell(col++).setCellValue(dto.getFechaInicioEspera2());
            row.createCell(col++).setCellValue(dto.getHoraInicioEspera2());
            row.createCell(col++).setCellValue(dto.getFechaFinEspera2());
            row.createCell(col++).setCellValue(dto.getHoraFinEspera2());

            row.createCell(col++).setCellValue(dto.getNombreUsuarioTicket());
            row.createCell(col++).setCellValue(dto.getNombreUsuarioRecepcion());
            row.createCell(col++).setCellValue(dto.getNombreUsuarioAtencion());
            row.createCell(col++).setCellValue(dto.getUsuarioEspera1());
            row.createCell(col++).setCellValue(dto.getUsuarioEspera2());

            row.createCell(col++).setCellValue(dto.getDescripcionTicket());
            row.createCell(col++).setCellValue(dto.getMensajeRecepcion());
            row.createCell(col++).setCellValue(dto.getDescripcionAtencion());
            row.createCell(col++).setCellValue(dto.getDescripcionEspera1());
            row.createCell(col++).setCellValue(dto.getDescripcionEspera2());

            row.createCell(col++).setCellValue(dto.getNombreFaseTicket());
            row.createCell(col++).setCellValue(dto.getNombreCategoriaIncidencia());
            row.createCell(col++).setCellValue(dto.getNombreSubCatIncidencia());
            row.createCell(col++).setCellValue(dto.getNombreTipoIncidencia());
            row.createCell(col++).setCellValue(dto.getNombreUrgencia());
            row.createCell(col++).setCellValue(dto.getNombreClasificacionAtencion());
            row.createCell(col++).setCellValue(dto.getNombreAreaAtencion());
            row.createCell(col++).setCellValue(dto.getNombreSedeAtencion());
            row.createCell(col++).setCellValue(dto.getClasificacionEspera1());
            row.createCell(col++).setCellValue(dto.getClasificacionEspera2());

            long totalEspera = 0;
            totalEspera += calcularDiferenciaSegundos(dto.getFechaInicioEspera1(), dto.getHoraInicioEspera1(), dto.getFechaFinEspera1(), dto.getHoraFinEspera1(), formatter);
            totalEspera += calcularDiferenciaSegundos(dto.getFechaInicioEspera2(), dto.getHoraInicioEspera2(), dto.getFechaFinEspera2(), dto.getHoraFinEspera2(), formatter);

            long totalTicketAtencion = calcularDiferenciaSegundos(dto.getFechaFormateadaTicket(), dto.getHoraFormateadaTicket(), dto.getFechaFormateadaAtencion(), dto.getHoraFormateadaAtencion(), formatter);
            long tiempoUtil = totalTicketAtencion - totalEspera;

            row.createCell(col++).setCellValue(totalEspera);
            row.createCell(col++).setCellValue(totalTicketAtencion);
            row.createCell(col++).setCellValue(Math.max(tiempoUtil, 0));
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream;
    }

    private long calcularDiferenciaSegundos(String fechaInicio, String horaInicio, String fechaFin, String horaFin, DateTimeFormatter formatter) {
        try {
            if (fechaInicio == null || horaInicio == null || fechaFin == null || horaFin == null) return 0;
            if (fechaInicio.equals("En curso") || fechaFin.equals("En curso")) return 0;
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio + " " + horaInicio, formatter);
            LocalDateTime fin = LocalDateTime.parse(fechaFin + " " + horaFin, formatter);
            return java.time.Duration.between(inicio, fin).getSeconds();
        } catch (Exception e) {
            return 0;
        }
    }
}
