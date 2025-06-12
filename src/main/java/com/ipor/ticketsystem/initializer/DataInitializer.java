package com.ipor.ticketsystem.initializer;

import com.ipor.ticketsystem.model.dynamic.Ticket;
import com.ipor.ticketsystem.model.dynamic.Usuario;
import com.ipor.ticketsystem.model.fixed.*;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import com.ipor.ticketsystem.repository.dynamic.UsuarioRepository;
import com.ipor.ticketsystem.repository.fixed.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

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
    private TipoIncidenciaRepository tipoIncidenciaRepository;

    @Autowired
    private CategoriaIncidenciaRepository categoriaIncidenciaRepository;

    @Autowired
    private SubCategoriaIncidenciaRepository subCategoriaIncidenciaRepository;

    @Autowired
    private ClasificacionAtencionRepository clasificacionAtencionRepository;

    @Autowired
    private AreaAtencionRepository areaAtencionRepository;

    @Autowired
    private SedeRepository sedeRepository;

    @Autowired
    private HorarioAtencionSoporteRepository horarioAtencionSoporteRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ClasificacionEsperaRepository clasificacionEsperaRepository;


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
            faseTicketRepository.save(new FaseTicket("En Espera"));

        }

        // 4. Crear tipos de urgencia
        if (clasificacionUrgenciaRepository.count() == 0) {
            clasificacionUrgenciaRepository.save(new ClasificacionUrgencia("Bajo", true));
            clasificacionUrgenciaRepository.save(new ClasificacionUrgencia("Medio", true));
            clasificacionUrgenciaRepository.save(new ClasificacionUrgencia("Alto", true));
            clasificacionUrgenciaRepository.save(new ClasificacionUrgencia("Critico", true));
        }

        // 5. Crear clasificación de desestimación
        if (clasificacionDesestimacionRepository.count() == 0) {
            clasificacionDesestimacionRepository.save(new ClasificacionDesestimacion("Usuario Desactivado", true));
            clasificacionDesestimacionRepository.save(new ClasificacionDesestimacion("Fuera de alcance del área de TI", true));
            clasificacionDesestimacionRepository.save(new ClasificacionDesestimacion("Solicitud Duplicada", true));
            clasificacionDesestimacionRepository.save(new ClasificacionDesestimacion("Error del Usuario", true));
            clasificacionDesestimacionRepository.save(new ClasificacionDesestimacion("Falta de Información y sin respuesta", true));
        }


        // 6. Crear clasificación de incidencia
        if (categoriaIncidenciaRepository.count() == 0) {
            // Categorías
            CategoriaIncidencia CatHardware = new CategoriaIncidencia("Relacionado a Hardware", true);
            CategoriaIncidencia CatSoftware = new CategoriaIncidencia("Relacionado a Software", true);
            CategoriaIncidencia CatRedes = new CategoriaIncidencia("Relacionado a Red", true);
            categoriaIncidenciaRepository.save(CatHardware);
            categoriaIncidenciaRepository.save(CatSoftware);
            categoriaIncidenciaRepository.save(CatRedes);

            // Subcategorías Hardware
            SubCategoriaIncidencia Computadora = new SubCategoriaIncidencia("Computadora", CatHardware, true);
            SubCategoriaIncidencia Impresora = new SubCategoriaIncidencia("Impresora", CatHardware, true);
            SubCategoriaIncidencia AnexoTelefonico = new SubCategoriaIncidencia("Anexo Telefónico", CatHardware, true);
            SubCategoriaIncidencia Perifericos = new SubCategoriaIncidencia("Periféricos", CatHardware, true);

            // Subcategorías Software
            SubCategoriaIncidencia Spring = new SubCategoriaIncidencia("Spring ERP", CatSoftware, true);
            SubCategoriaIncidencia Mosaiq = new SubCategoriaIncidencia("MOSAIQ", CatSoftware, true);

            // Subcategorías Redes
            SubCategoriaIncidencia Cableado = new SubCategoriaIncidencia("Cableado", CatRedes, true);
            SubCategoriaIncidencia Wifi = new SubCategoriaIncidencia("Conexión Wi-Fi", CatRedes, true);
            SubCategoriaIncidencia RedLocal = new SubCategoriaIncidencia("Red Local", CatRedes, true);

            subCategoriaIncidenciaRepository.saveAll(Arrays.asList(
                    Computadora, Impresora, AnexoTelefonico, Perifericos,
                    Spring, Mosaiq,
                    Cableado, Wifi, RedLocal
            ));

            // Tipos de incidencia por subcategoría

            // Computadora
            tipoIncidenciaRepository.save(new TipoIncidencia("Emisión de sonidos extraños", Computadora, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("No enciende", Computadora, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Pantalla en negro", Computadora, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Presenta lentitud", Computadora, true));

            // Impresora
            tipoIncidenciaRepository.save(new TipoIncidencia("Atasco de papel", Impresora, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("No imprime", Impresora, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Falta de tinta o tóner", Impresora, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Problemas de conexión USB o red", Impresora, true));

            // Anexo Telefónico
            tipoIncidenciaRepository.save(new TipoIncidencia("Sin tono de llamada", AnexoTelefonico, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Anexo no comunica", AnexoTelefonico, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Corte en la llamada", AnexoTelefonico, true));

            // Periféricos
            tipoIncidenciaRepository.save(new TipoIncidencia("Mouse no responde", Perifericos, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Teclado no responde", Perifericos, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Parlante no emite sonido", Perifericos, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Camara no emite video", Perifericos, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Falla en lector de huella", Perifericos, true));

            //-------------------------------

            // Spring ERP
            tipoIncidenciaRepository.save(new TipoIncidencia("Solicitud de capacitación", Spring, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Error en carga de datos", Spring, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Problemas con credenciales", Spring, true));

            // MOSAIQ
            tipoIncidenciaRepository.save(new TipoIncidencia("Error al iniciar sesión", Mosaiq, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Solicitud de cambio de datos", Mosaiq, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Problemas con visualización de pacientes", Mosaiq, true));

            //-------------------------------


            // Cableado
            tipoIncidenciaRepository.save(new TipoIncidencia("Cable de red dañado", Cableado, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Conector RJ45 roto", Cableado, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Puerto de red sin señal", Cableado, true));

            // Conexión Wi-Fi
            tipoIncidenciaRepository.save(new TipoIncidencia("No se conecta a la red", Wifi, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Conexión lenta o intermitente", Wifi, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Red Wi-Fi no aparece", Wifi, true));

            // Red Local
            tipoIncidenciaRepository.save(new TipoIncidencia("PC no recibe IP", RedLocal, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Red no identificada", RedLocal, true));
            tipoIncidenciaRepository.save(new TipoIncidencia("Sin acceso a carpetas compartidas", RedLocal, true));
        }

        // 7. Crear clasificacion de Atencion
        if (clasificacionAtencionRepository.count() == 0) {
            clasificacionAtencionRepository.save(new ClasificacionAtencion("Incidencia Resuelta", true));
            clasificacionAtencionRepository.save(new ClasificacionAtencion("Elevado a Proveedor", true));
            clasificacionAtencionRepository.save(new ClasificacionAtencion("Requiere Revisión Posterior", true));
            clasificacionAtencionRepository.save(new ClasificacionAtencion("Pendiente por Repuestos", true));
            clasificacionAtencionRepository.save(new ClasificacionAtencion("Configuración Realizada", true));
            clasificacionAtencionRepository.save(new ClasificacionAtencion("Derivado a Otra Área", true));
            clasificacionAtencionRepository.save(new ClasificacionAtencion("No Se Encontró Falla", true));
        }

        // 8. Crear clasificacion de Sede
        if (sedeRepository.count() == 0) {
            sedeRepository.save(new Sede("San Isidro", true));
            sedeRepository.save(new Sede("San Isidro - Administrativo", true));
            sedeRepository.save(new Sede("Jesús María", true));
        }

        // 9. Crear clasificación de Área
        if (areaAtencionRepository.count() == 0) {
            Sede sanIsidro = sedeRepository.findByNombre("San Isidro");

            areaAtencionRepository.save(new AreaAtencion("TI", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("RRHH", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Logística", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Calidad", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Quimioterapia", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Mantenimiento", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Investigación 4to", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Consultorios 4to", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Gerencia", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Asistentes Gerencia", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Consultorios 3er", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Investigación 3er", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Asistentes Dr Rodriguez", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Físicos", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Consultorios 2do", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("PET CT", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Coordinadoras", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Farmacia", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Admisión", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Imágenes", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Tomografía", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Radiografía", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Cochera", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Radioterapia", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Braquiterapia", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Archivo", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Facturación", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Contabilidad", sanIsidro, true));
            areaAtencionRepository.save(new AreaAtencion("Ventas", sanIsidro, true));
        }

        // 10. Crear Horario de atencion
        if (horarioAtencionSoporteRepository.count() == 0) {
            Usuario admin = usuarioRepository.findById(1L).get();
            LocalTime horaEntrada = LocalTime.of(8, 0);
            LocalTime horaSalida = LocalTime.of(17, 0);

            // Crear horario
            HorarioAtencionSoporte horario = new HorarioAtencionSoporte(horaEntrada, horaSalida, admin, LocalDateTime.now());
            horario = horarioAtencionSoporteRepository.save(horario);

            // Asignar a todos los tickets
            List<Ticket> tickets = ticketRepository.findAll();
            for (Ticket ticket : tickets) {
                ticket.setHorarioAtencionSoporte(horario);
            }
            ticketRepository.saveAll(tickets);
        }

        // 11. Crear clasificacion de Atencion
        if (clasificacionEsperaRepository.count() == 0) {
            clasificacionEsperaRepository.save(new ClasificacionEspera("Fuera de horario de atención", true));
            clasificacionEsperaRepository.save(new ClasificacionEspera("Espera de autorización", true));
            clasificacionEsperaRepository.save(new ClasificacionEspera("Espera de proveedor", true));

        }

    }
}