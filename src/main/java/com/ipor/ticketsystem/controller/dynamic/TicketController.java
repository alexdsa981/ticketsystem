package com.ipor.ticketsystem.controller.dynamic;

import com.ipor.ticketsystem.dto.dynamic.TicketRecord;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.model.fixed.ClasificacionIncidencia;
import com.ipor.ticketsystem.model.fixed.FaseTicket;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import com.ipor.ticketsystem.repository.dynamic.UsuarioRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionIncidenciaRepository;
import com.ipor.ticketsystem.repository.fixed.FaseTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClasificacionIncidenciaRepository clasificacionIncidenciaRepository;
    private final FaseTicketRepository faseTicketRepository;

    @Autowired
    public TicketController(TicketRepository ticketRepository, UsuarioRepository usuarioRepository,
                            ClasificacionIncidenciaRepository clasificacionIncidenciaRepository, FaseTicketRepository faseTicketRepository) {
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
        this.clasificacionIncidenciaRepository = clasificacionIncidenciaRepository;
        this.faseTicketRepository = faseTicketRepository;
    }

    // Obtener todos los tickets
    @GetMapping
    public ResponseEntity<List<TicketRecord>> getAllTickets() {
        List<TicketRecord> tickets = ticketRepository.findAll()
                .stream()
                .map(TicketRecord::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tickets);
    }

    // Obtener un ticket por ID
    @GetMapping("/{id}")
    public ResponseEntity<TicketRecord> getTicketById(@PathVariable Long id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        return ticket.map(TicketRecord::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo ticket
    @PostMapping
    public ResponseEntity<TicketRecord> createTicket(@RequestBody TicketRecord ticketRecord) {
        Ticket ticket = new Ticket();
        ticket.setDescripcion(ticketRecord.descripcion());
        // Establecer usuario logueado
        String username = obtenerUsernameDelToken();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        ticket.setUsuario(usuario);

        // Asignar la fase del ticket a 1 de forma automática
        FaseTicket faseInicial = faseTicketRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Fase inicial no encontrada"));
        ticket.setFaseTicket(faseInicial);

        // Asignar la clasificación de incidencia
        ClasificacionIncidencia clasificacion = clasificacionIncidenciaRepository.findById(ticketRecord.clasificacionIncidenciaId())
                .orElseThrow(() -> new RuntimeException("Clasificación de incidencia no encontrada"));
        ticket.setClasificacionIncidencia(clasificacion);

        // La fecha y la hora se establecen automáticamente en el método prePersist
        Ticket savedTicket = ticketRepository.save(ticket);
        TicketRecord createdRecord = new TicketRecord(savedTicket);
        return ResponseEntity.ok(createdRecord);
    }


    // Actualizar un ticket existente
    @PutMapping("/{id}")
    public ResponseEntity<TicketRecord> updateTicket(@PathVariable Long id, @RequestBody TicketRecord ticketRecord) {
        if (!ticketRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        System.out.println(ticketRecord.clasificacionIncidenciaId()+ " " +ticketRecord.faseTicketId());
        Ticket ticket = ticketRepository.findById(id).orElseThrow();
        ticket.setDescripcion(ticketRecord.descripcion());
        ticket.setClasificacionIncidencia(clasificacionIncidenciaRepository.findById(ticketRecord.clasificacionIncidenciaId()).orElseThrow());
        ticket.setFaseTicket(faseTicketRepository.findById(ticketRecord.faseTicketId()).orElseThrow());

        Ticket savedTicket = ticketRepository.save(ticket);
        TicketRecord updatedRecord = new TicketRecord(savedTicket);
        return ResponseEntity.ok(updatedRecord);
    }

    // Eliminar un ticket por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        if (!ticketRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ticketRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private String obtenerUsernameDelToken() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
