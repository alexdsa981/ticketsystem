package com.ipor.ticketsystem.controller.dynamic;

import com.ipor.ticketsystem.dto.dynamic.ServicioRecord;
import com.ipor.ticketsystem.model.dynamic.Servicio;
import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.model.fixed.FaseTicket;
import com.ipor.ticketsystem.repository.dynamic.RecepcionRepository;
import com.ipor.ticketsystem.repository.dynamic.ServicioRepository;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import com.ipor.ticketsystem.repository.dynamic.UsuarioRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionServicioRepository;
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
@RequestMapping("/api/servicios")
public class ServicioController {

    private final ServicioRepository servicioRepository;
    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;
    private final FaseTicketRepository faseTicketRepository;
    private final RecepcionRepository recepcionRepository;
    private final ClasificacionServicioRepository clasificacionServicioRepository;

    @Autowired
    public ServicioController(ServicioRepository servicioRepository, TicketRepository ticketRepository,
                              UsuarioRepository usuarioRepository, FaseTicketRepository faseTicketRepository, RecepcionRepository recepcionRepository, ClasificacionServicioRepository clasificacionServicioRepository) {
        this.servicioRepository = servicioRepository;
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
        this.faseTicketRepository = faseTicketRepository;
        this.recepcionRepository = recepcionRepository;
        this.clasificacionServicioRepository = clasificacionServicioRepository;
    }

    // Crear nuevo servicio y actualizar la fase del ticket a 3 (Atendido)
    @PostMapping
    public ResponseEntity<ServicioRecord> createServicio(@RequestBody ServicioRecord servicioRecord) {
        Servicio servicio = new Servicio();
        servicio.setDescripcion(servicioRecord.descripcion());
        servicio.setClasificacionServicio(clasificacionServicioRepository.findById(servicioRecord.clasificacionAtencionId()).get());
        // Establecer el ticket relacionado
        Ticket ticket = ticketRepository.findById(servicioRecord.ticketId())
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
        servicio.setTicket(ticket);

        // Verificar si existe una Recepcion con el ticketId
        boolean recepcionExists = recepcionRepository.existsByTicketId(servicioRecord.ticketId());


        if (!recepcionExists) {
            throw new RuntimeException("No se encontró recepción para el ticket con ID " + servicioRecord.ticketId());
        }





        // Establecer el usuario logueado
        String username = obtenerUsernameDelToken();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        servicio.setUsuario(usuario);

        // Actualizar la fase del ticket a 3 (Atendido)
        FaseTicket faseAtencion = faseTicketRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("Fase de atención no encontrada"));
        ticket.setFaseTicket(faseAtencion);
        ticketRepository.save(ticket);

        // Guardar el servicio
        Servicio savedServicio = servicioRepository.save(servicio);
        ServicioRecord createdRecord = new ServicioRecord(savedServicio);
        return ResponseEntity.ok(createdRecord);
    }

    // Obtener todos los servicios
    @GetMapping
    public ResponseEntity<List<ServicioRecord>> getAllServicios() {
        List<ServicioRecord> servicios = servicioRepository.findAll()
                .stream()
                .map(ServicioRecord::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(servicios);
    }

    // Obtener un servicio por ID
    @GetMapping("/{id}")
    public ResponseEntity<ServicioRecord> getServicioById(@PathVariable Long id) {
        Optional<Servicio> servicio = servicioRepository.findById(id);
        return servicio.map(ServicioRecord::new)
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
