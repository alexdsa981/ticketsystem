package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
public class TicketExportController {

    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping("/admin/exportar-tickets")
    public ResponseEntity<byte[]> exportTicketsToExcel() {
        try {
            ByteArrayOutputStream outputStream = excelExportService.exportToExcel();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=tickets.xlsx");

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}