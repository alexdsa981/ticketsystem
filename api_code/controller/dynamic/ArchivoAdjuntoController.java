package com.ipor.ticketsystem.api_code.controller.dynamic;

import com.ipor.ticketsystem.api_code.dto.dynamic.ArchivoAdjuntoRecord;
import com.ipor.ticketsystem.model.dynamic.ArchivoAdjunto;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.repository.dynamic.ArchivoAdjuntoRepository;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/archivosAdjuntos")
public class ArchivoAdjuntoController {

    private final ArchivoAdjuntoRepository archivoAdjuntoRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public ArchivoAdjuntoController(ArchivoAdjuntoRepository archivoAdjuntoRepository, TicketRepository ticketRepository) {
        this.archivoAdjuntoRepository = archivoAdjuntoRepository;
        this.ticketRepository = ticketRepository;
    }

    // Crear un nuevo archivo adjunto, se debe codificar el archivo en base64,
    // en la base de datos se pasará automaticamente a hex y al llamar en consulta
    // se transformará denuevo a base64, entonces si se decodifica el texto base 64,
    // dará el archivo que se ha subido
    @PostMapping
    public ResponseEntity<ArchivoAdjuntoRecord> createArchivoAdjunto(@RequestBody ArchivoAdjuntoRecord archivoAdjuntoRecord) {
        ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
        archivoAdjunto.setNombre(archivoAdjuntoRecord.nombre());
        archivoAdjunto.setArchivo(archivoAdjuntoRecord.archivo());
        archivoAdjunto.setTipoContenido(archivoAdjuntoRecord.tipoContenido());

        // Establecer el ticket relacionado
        Ticket ticket = ticketRepository.findById(archivoAdjuntoRecord.ticketId())
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
        archivoAdjunto.setTicket(ticket);

        // Guardar el archivo adjunto
        ArchivoAdjunto savedArchivoAdjunto = archivoAdjuntoRepository.save(archivoAdjunto);
        ArchivoAdjuntoRecord createdRecord = new ArchivoAdjuntoRecord(savedArchivoAdjunto);
        return ResponseEntity.ok(createdRecord);
    }

    // Obtener todos los archivos adjuntos
    @GetMapping
    public ResponseEntity<List<ArchivoAdjuntoRecord>> getAllArchivosAdjuntos() {
        List<ArchivoAdjuntoRecord> archivosAdjuntos = archivoAdjuntoRepository.findAll()
                .stream()
                .map(ArchivoAdjuntoRecord::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(archivosAdjuntos);
    }

    // Obtener un archivo adjunto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ArchivoAdjuntoRecord> getArchivoAdjuntoById(@PathVariable Long id) {
        Optional<ArchivoAdjunto> archivoAdjunto = archivoAdjuntoRepository.findById(id);
        return archivoAdjunto.map(ArchivoAdjuntoRecord::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
