package com.ipor.ticketsystem.initializer;

import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.model.fixed.ClasificacionDesestimacion;
import com.ipor.ticketsystem.model.fixed.ClasificacionUrgencia;
import com.ipor.ticketsystem.model.fixed.FaseTicket;
import com.ipor.ticketsystem.model.fixed.RolUsuario;
import com.ipor.ticketsystem.repository.dynamic.UsuarioRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionDesestimacionRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionUrgenciaRepository;
import com.ipor.ticketsystem.repository.fixed.FaseTicketRepository;
import com.ipor.ticketsystem.repository.fixed.RolUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RolUsuarioRepository rolUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FaseTicketRepository faseTicketRepository;

    @Autowired
    private ClasificacionUrgenciaRepository clasificacionUrgenciaRepository;

    @Autowired
    private ClasificacionDesestimacionRepository clasificacionDesestimacionRepository;

    @Override
    public void run(String... args) {
        // 1. Crear roles si no existen
        if (rolUsuarioRepository.count() == 0) {
            rolUsuarioRepository.save(new RolUsuario("Usuario"));
            rolUsuarioRepository.save(new RolUsuario("Soporte"));
            rolUsuarioRepository.save(new RolUsuario("Admin"));
        }

        // 2. Crear usuario administrador si no existe
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            RolUsuario rolAdmin = rolUsuarioRepository.findByNombre("Admin");
            Usuario admin = new Usuario();
            admin.setIsActive(true);
            admin.setNombre("ADMINISTRADOR");
            admin.setUsername("admin");
            admin.setPassword("$2a$12$7SW6dd16qcrYSdV0L4Uzp.qzCEe6ricYOH9fdr1r/bGlF2ItBun4a"); // Contraseña ya encriptada
            admin.setRolUsuario(rolAdmin);
            admin.setChangedPass(false);
            usuarioRepository.save(admin);
        }

        // 3. Crear fases de ticket
        if (faseTicketRepository.count() == 0) {
            faseTicketRepository.save(new FaseTicket("Enviado"));
            faseTicketRepository.save(new FaseTicket("Recepcionado - En Proceso"));
            faseTicketRepository.save(new FaseTicket("Cerrado - Atendido"));
            faseTicketRepository.save(new FaseTicket("Desestimado"));
        }

        // 4. Crear tipos de urgencia
        if (clasificacionUrgenciaRepository.count() == 0) {
            clasificacionUrgenciaRepository.save(new ClasificacionUrgencia("Baja", true));
            clasificacionUrgenciaRepository.save(new ClasificacionUrgencia("Media", true));
            clasificacionUrgenciaRepository.save(new ClasificacionUrgencia("Alta", true));
        }

        // 5. Crear clasificación de desestimación
        if (clasificacionDesestimacionRepository.count() == 0) {
            clasificacionDesestimacionRepository.save(new ClasificacionDesestimacion("Usuario Desactivado", true));
        }
    }
}