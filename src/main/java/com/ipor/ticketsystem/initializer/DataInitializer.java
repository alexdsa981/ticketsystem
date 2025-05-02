package com.ipor.ticketsystem.initializer;

import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.model.fixed.*;
import com.ipor.ticketsystem.repository.dynamic.UsuarioRepository;
import com.ipor.ticketsystem.repository.fixed.*;
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

    @Autowired
    private ClasificacionIncidenciaRepository clasificacionIncidenciaRepository;

    @Autowired
    private ClasificacionServicioRepository clasificacionServicioRepository;

    @Autowired
    private ClasificacionAreaRepository clasificacionAreaRepository;

    @Override
    public void run(String... args) {
        // 1. Crear roles si no existen
        if (rolUsuarioRepository.count() == 0) {
            rolUsuarioRepository.save(new RolUsuario("Usuario"));
            rolUsuarioRepository.save(new RolUsuario("Soporte"));
            rolUsuarioRepository.save(new RolUsuario("Admin"));
        }

        // 2. Crear usuario ADMINISTRADOR si no existe y usuario SISTEMAS
        if (usuarioRepository.count() == 0) {
            RolUsuario rolAdmin = rolUsuarioRepository.findByNombre("Admin");
            Usuario admin = new Usuario();
            admin.setNombre("ADMINISTRADOR");
            admin.setUsername("ADMIN");
            admin.setPassword("$2a$12$7SW6dd16qcrYSdV0L4Uzp.qzCEe6ricYOH9fdr1r/bGlF2ItBun4a"); // Contraseña ya encriptada
            admin.setRolUsuario(rolAdmin);
            admin.setIsActive(true);
            admin.setChangedPass(false);
            admin.setIsSpringUser(false);
            usuarioRepository.save(admin);

            RolUsuario rolSoporte = rolUsuarioRepository.findByNombre("Soporte");
            Usuario soporte = new Usuario();
            soporte.setIsSpringUser(false);
            soporte.setIsActive(true);
            soporte.setChangedPass(false);
            soporte.setPassword("$2a$12$7SW6dd16qcrYSdV0L4Uzp.qzCEe6ricYOH9fdr1r/bGlF2ItBun4a"); // Contraseña ya encriptada
            soporte.setRolUsuario(rolSoporte);
            soporte.setNombre("SOPORTE");
            soporte.setUsername("SOPORTE");
            usuarioRepository.save(soporte);
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
            clasificacionDesestimacionRepository.save(new ClasificacionDesestimacion("Fuera de alcance del área de TI", true));
        }

        // 6. Crear clasificación de incidencia
        if (clasificacionIncidenciaRepository.count() == 0) {
            clasificacionIncidenciaRepository.save(new ClasificacionIncidencia("Relacionado a Red", true));
            clasificacionIncidenciaRepository.save(new ClasificacionIncidencia("Relacionado a Software", true));
        }
        // 7. Crear clasificacion de Servicio
        if (clasificacionServicioRepository.count() == 0) {
            clasificacionServicioRepository.save(new ClasificacionServicio("Incidencia Resuelta", true));
        }
        // 8. Crear clasificacion de Área
        if (clasificacionAreaRepository.count() == 0) {
            clasificacionAreaRepository.save(new ClasificacionArea("TI", true));
            clasificacionAreaRepository.save(new ClasificacionArea("RRHH", true));
            clasificacionAreaRepository.save(new ClasificacionArea("Logística", true));
        }

    }
}