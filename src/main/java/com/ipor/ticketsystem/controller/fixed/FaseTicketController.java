package com.ipor.ticketsystem.controller.fixed;

import com.ipor.ticketsystem.dto.fixed.FaseTicketRecord;
import com.ipor.ticketsystem.model.fixed.FaseTicket;
import com.ipor.ticketsystem.repository.fixed.FaseTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fases-tickets")
public class FaseTicketController {

    private final FaseTicketRepository faseTicketRepository;

    @Autowired
    public FaseTicketController(FaseTicketRepository faseTicketRepository) {
        this.faseTicketRepository = faseTicketRepository;
    }

    // Obtener todas las fases de ticket
    @GetMapping
    public ResponseEntity<List<FaseTicketRecord>> getAllFasesTickets() {
        List<FaseTicketRecord> fases = faseTicketRepository.findAll()
                .stream()
                .map(FaseTicketRecord::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(fases);
    }

    // Obtener una fase de ticket por ID
    @GetMapping("/{id}")
    public ResponseEntity<FaseTicketRecord> getFaseTicketById(@PathVariable Long id) {
        Optional<FaseTicket> faseTicket = faseTicketRepository.findById(id);
        return faseTicket.map(FaseTicketRecord::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva fase de ticket
    @PostMapping
    public ResponseEntity<FaseTicketRecord> createFaseTicket(@RequestBody FaseTicketRecord faseTicketRecord) {
        FaseTicket faseTicket = new FaseTicket();
        faseTicket.setNombre(faseTicketRecord.nombre());
        FaseTicket savedFaseTicket = faseTicketRepository.save(faseTicket);
        FaseTicketRecord createdRecord = new FaseTicketRecord(savedFaseTicket);
        return ResponseEntity.ok(createdRecord);
    }

    // Actualizar una fase de ticket existente
    @PutMapping("/{id}")
    public ResponseEntity<FaseTicketRecord> updateFaseTicket(@PathVariable Long id, @RequestBody FaseTicketRecord faseTicketRecord) {
        if (!faseTicketRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        FaseTicket faseTicket = new FaseTicket();
        faseTicket.setId(id);
        faseTicket.setNombre(faseTicketRecord.nombre());
        FaseTicket savedFaseTicket = faseTicketRepository.save(faseTicket);
        FaseTicketRecord updatedRecord = new FaseTicketRecord(savedFaseTicket);
        return ResponseEntity.ok(updatedRecord);
    }

    // Eliminar una fase de ticket por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaseTicket(@PathVariable Long id) {
        if (!faseTicketRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        faseTicketRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
