package com.ipor.ticketsystem.api_code.controller.dynamic;

import com.ipor.ticketsystem.api_code.dto.dynamic.RecepcionRecord;
import com.ipor.ticketsystem.model.dynamic.Recepcion;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.model.fixed.FaseTicket;
import com.ipor.ticketsystem.repository.dynamic.RecepcionRepository;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import com.ipor.ticketsystem.repository.dynamic.UsuarioRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionUrgenciaRepository;
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
@RequestMapping("/app/recepciones")
public class RecepcionController {

    private final RecepcionRepository recepcionRepository;
    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;
    private final FaseTicketRepository faseTicketRepository;
    private final ClasificacionUrgenciaRepository clasificacionUrgenciaRepository;

    @Autowired
    public RecepcionController(RecepcionRepository recepcionRepository, TicketRepository ticketRepository,
                               UsuarioRepository usuarioRepository, FaseTicketRepository faseTicketRepository, ClasificacionUrgenciaRepository clasificacionUrgenciaRepository) {
        this.recepcionRepository = recepcionRepository;
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
        this.faseTicketRepository = faseTicketRepository;
        this.clasificacionUrgenciaRepository = clasificacionUrgenciaRepository;
    }

    // Crear nueva recepción y actualizar la fase del ticket
    @PostMapping
    public ResponseEntity<RecepcionRecord> createRecepcion(@RequestBody RecepcionRecord recepcionRecord) {
        Recepcion recepcion = new Recepcion();
        recepcion.setMensaje(recepcionRecord.mensaje());
        //
        recepcion.setClasificacionUrgencia(clasificacionUrgenciaRepository.findById(recepcionRecord.clasificacionUrgenciaId()).get());
        // Establecer el ticket relacionado
        Ticket ticket = ticketRepository.findById(recepcionRecord.ticketId())
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
        recepcion.setTicket(ticket);

        // Establecer el usuario logueado
        String username = obtenerUsernameDelToken();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        recepcion.setUsuario(usuario);

        // Actualizar la fase del ticket a 2 (Recepcionado)
        FaseTicket faseRecepcion = faseTicketRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Fase de recepción no encontrada"));
        ticket.setFaseTicket(faseRecepcion);
        ticketRepository.save(ticket);

        // Guardar la recepción
        Recepcion savedRecepcion = recepcionRepository.save(recepcion);
        RecepcionRecord createdRecord = new RecepcionRecord(savedRecepcion);
        return ResponseEntity.ok(createdRecord);
    }

    // Obtener todas las recepciones
    @GetMapping
    public ResponseEntity<List<RecepcionRecord>> getAllRecepciones() {
        List<RecepcionRecord> recepciones = recepcionRepository.findAll()
                .stream()
                .map(RecepcionRecord::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recepciones);
    }

    // Obtener una recepción por ID
    @GetMapping("/{id}")
    public ResponseEntity<RecepcionRecord> getRecepcionById(@PathVariable Long id) {
        Optional<Recepcion> recepcion = recepcionRepository.findById(id);
        return recepcion.map(RecepcionRecord::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
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
