package com.ipor.ticketsystem.service;

import com.ipor.ticketsystem.model.dto.otros.ExcelExport.TicketExportDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class ExcelExportService {

    private final JdbcTemplate jdbcTemplate;

    public ExcelExportService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ByteArrayOutputStream exportToExcel() throws IOException {
        List<TicketExportDTO> tickets = jdbcTemplate.query("SELECT * FROM vw_ticket_detalle", this::mapRowToTicket);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Tickets");

        // Crear estilos
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setWrapText(true); // Opcional: Ajustar texto en celdas




        // Crear encabezados
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID Ticket", "Fase", "Fecha Ticket", "Hora Ticket", "Usuario Ticket", "Clasificación Incidencia",
                "Fecha Recepción", "Hora Recepción", "Usuario Recepción", "Clasificación Urgencia",
                "Fecha Servicio", "Hora Servicio", "Usuario Servicio", "Clasificación Servicio",
                "Tiempo Espera Recepción (seg.)", "Tiempo Espera Servicio (seg.)"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        // Ajustar ancho de columnas (en unidades de 1/256 de carácter)
        sheet.setColumnWidth(0, 15 * 256); // ID Ticket
        sheet.setColumnWidth(1, 20 * 256); // Fase
        sheet.setColumnWidth(2, 25 * 256); // Fecha Ticket
        sheet.setColumnWidth(3, 15 * 256); // Hora Ticket
        sheet.setColumnWidth(4, 25 * 256); // Usuario Ticket
        sheet.setColumnWidth(5, 25 * 256); // Clasificación Incidencia
        sheet.setColumnWidth(6, 25 * 256); // Fecha Recepción
        sheet.setColumnWidth(7, 15 * 256); // Hora Recepción
        sheet.setColumnWidth(8, 25 * 256); // Usuario Recepción
        sheet.setColumnWidth(9, 25 * 256); // Clasificación Urgencia
        sheet.setColumnWidth(10, 25 * 256); // Fecha Servicio
        sheet.setColumnWidth(11, 15 * 256); // Hora Servicio
        sheet.setColumnWidth(12, 25 * 256); // Usuario Servicio
        sheet.setColumnWidth(13, 25 * 256); // Clasificación Servicio
        sheet.setColumnWidth(14, 25 * 256); // Tiempo Espera Recepción
        sheet.setColumnWidth(15, 25 * 256); // Tiempo Espera Servicio






        //formato de fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        // Agregar datos
        int rowNum = 1;
        for (TicketExportDTO ticket : tickets) {
            Row row = sheet.createRow(rowNum++);

// ID Ticket
            row.createCell(0).setCellValue(ticket.getIdTicket());

// Fase
            row.createCell(1).setCellValue(ticket.getFaseTicket());

// Fecha Ticket
            if (ticket.getFechaTicket() != null) {
                row.createCell(2).setCellValue(dateFormat.format(ticket.getFechaTicket()));
            } else {
                row.createCell(2).setBlank(); // Dejar la celda vacía
            }

// Hora Ticket
            if (ticket.getHoraTicket() != null) {
                row.createCell(3).setCellValue(timeFormat.format(ticket.getHoraTicket()));
            } else {
                row.createCell(3).setBlank(); // Dejar la celda vacía
            }

// Usuario Ticket
            row.createCell(4).setCellValue(ticket.getUsuarioTicket());

// Clasificación Incidencia
            row.createCell(5).setCellValue(ticket.getClasIncidencia());

// Fecha Recepción
            if (ticket.getFechaRecepcion() != null) {
                row.createCell(6).setCellValue(dateFormat.format(ticket.getFechaRecepcion()));
            } else {
                row.createCell(6).setBlank(); // Dejar la celda vacía
            }

// Hora Recepción
            if (ticket.getHoraRecepcion() != null) {
                row.createCell(7).setCellValue(timeFormat.format(ticket.getHoraRecepcion()));
            } else {
                row.createCell(7).setBlank(); // Dejar la celda vacía
            }

// Usuario Recepción
            row.createCell(8).setCellValue(ticket.getUsuarioRecepcion());

// Clasificación Urgencia
            row.createCell(9).setCellValue(ticket.getClasUrgencia());

// Fecha Servicio
            if (ticket.getFechaServicio() != null) {
                row.createCell(10).setCellValue(dateFormat.format(ticket.getFechaServicio()));
            } else {
                row.createCell(10).setBlank(); // Dejar la celda vacía
            }

// Hora Servicio
            if (ticket.getHoraServicio() != null) {
                row.createCell(11).setCellValue(timeFormat.format(ticket.getHoraServicio()));
            } else {
                row.createCell(11).setBlank(); // Dejar la celda vacía
            }

// Usuario Servicio
            row.createCell(12).setCellValue(ticket.getUsuarioServicio());

// Clasificación Servicio
            row.createCell(13).setCellValue(ticket.getClasServicio());

            // Tiempo Espera Recepción
            if (ticket.getTiempoEsperaRecepcion() == null) {
                row.createCell(14).setCellValue("");  // Se establece como vacío en lugar de cero
            } else {
                row.createCell(14).setCellValue(ticket.getTiempoEsperaRecepcion());
            }

            // Tiempo Espera Servicio
            if (ticket.getTiempoEsperaServicio() == null) {
                row.createCell(15).setCellValue("");  // Se establece como vacío en lugar de cero
            } else {
                row.createCell(15).setCellValue(ticket.getTiempoEsperaServicio());
            }
        }

        // Escribir en el flujo de salida
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream;
    }

    private TicketExportDTO mapRowToTicket(ResultSet rs, int rowNum) throws SQLException {
        TicketExportDTO ticket = new TicketExportDTO();
        ticket.setIdTicket(rs.getInt("id_ticket"));
        ticket.setFaseTicket(rs.getString("fase_ticket"));
        ticket.setFechaTicket(rs.getDate("fecha_ticket"));
        ticket.setHoraTicket(rs.getTime("hora_ticket"));
        ticket.setUsuarioTicket(rs.getString("usuario_ticket"));
        ticket.setClasIncidencia(rs.getString("clas_incidencia"));
        ticket.setFechaRecepcion(rs.getDate("fecha_recepcion"));
        ticket.setHoraRecepcion(rs.getTime("hora_recepcion"));
        ticket.setUsuarioRecepcion(rs.getString("usuario_recepcion"));
        ticket.setClasUrgencia(rs.getString("clas_urgencia"));
        ticket.setFechaServicio(rs.getDate("fecha_servicio"));
        ticket.setHoraServicio(rs.getTime("hora_servicio"));
        ticket.setUsuarioServicio(rs.getString("usuario_servicio"));
        ticket.setClasServicio(rs.getString("clas_servicio"));

        // Cambios aquí para manejar nulos
        Integer tiempoEsperaRecepcion = (Integer) rs.getObject("tiempo_espera_recepcion");
        Integer tiempoEsperaServicio = (Integer) rs.getObject("tiempo_espera_servicio");

        ticket.setTiempoEsperaRecepcion(tiempoEsperaRecepcion); // Esto puede ser null
        ticket.setTiempoEsperaServicio(tiempoEsperaServicio);   // Esto puede ser null

        return ticket;
    }
}