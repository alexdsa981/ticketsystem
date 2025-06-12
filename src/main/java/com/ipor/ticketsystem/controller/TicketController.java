package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.WebSocket.WSNotificacionesService;
import com.ipor.ticketsystem.model.dto.DetalleTicketDTO;
import com.ipor.ticketsystem.model.dto.otros.WebSocket.AtencionRecordWS;
import com.ipor.ticketsystem.model.dto.otros.WebSocket.DesestimacionRecordWS;
import com.ipor.ticketsystem.model.dto.otros.WebSocket.RecepcionRecordWS;
import com.ipor.ticketsystem.model.dto.otros.WebSocket.TicketRecordWS;
import com.ipor.ticketsystem.model.dynamic.*;
import com.ipor.ticketsystem.model.fixed.ClasificacionEspera;
import com.ipor.ticketsystem.model.fixed.HorarioAtencionSoporte;
import com.ipor.ticketsystem.repository.dynamic.DetalleEnEsperaRepository;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import com.ipor.ticketsystem.repository.fixed.ClasificacionEsperaRepository;
import com.ipor.ticketsystem.repository.fixed.HorarioAtencionSoporteRepository;
import com.ipor.ticketsystem.service.NotificacionesService;
import com.ipor.ticketsystem.service.TicketService;
import com.ipor.ticketsystem.service.UsuarioService;
import org.springframework.core.io.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/app/tickets")
public class TicketController {
    @Autowired
    TicketService ticketService;
    @Autowired
    HorarioAtencionSoporteRepository horarioAtencionSoporteRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    DetalleEnEsperaRepository detalleEnEsperaRepository;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    WSNotificacionesService WSNotificacionesService;
    @Autowired
    NotificacionesService notificacionesService;
    @Autowired
    ClasificacionEsperaRepository clasificacionEsperaRepository;

    //ESTA CLASE REPRESENTA LA LOGICA PARA MOSTRAR TICKETS CREADOS/REDIRIGIDOS (AÚN SIN RECEPCIONAR) EN LAS VISTAS
    public Model retornaTicketsPropiosAVista(Model model) {
        List<DetalleTicketDTO> MisTicketsDTO = ticketService.getMyTickets();
        model.addAttribute("MyTickets", MisTicketsDTO);
        return model;
    }

    public Model retornaTicketsRecibidosAVista(Model model) {
        List<DetalleTicketDTO> AllTicketsDTO = ticketService.getAllTicketsSinRecepcionar();
        model.addAttribute("AllTickets", AllTicketsDTO);
        return model;
    }


    @GetMapping("/buscar-ticket")
    @ResponseBody
    public String buscarTicket(@RequestParam("codigoTicket") Integer codigoTicket) {
        String codigo = String.format("TK-%04d", codigoTicket);
        Optional<Ticket> optionalTicket = ticketRepository.findByCodigoTicket(codigo);

        Usuario usuarioLogeado = usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado());
        boolean esSoporteOAdmin = usuarioLogeado.getRolUsuario().getId() == 2 || usuarioLogeado.getRolUsuario().getId() == 3;

        if (optionalTicket.isEmpty()) {
            // Usuarios comunes no deben saber si existe o no
            return esSoporteOAdmin ? "<p>Ticket no existente.</p>" : "<p>No se encontró un ticket asociado a tu cuenta con ese código.</p>";
        }

        DetalleTicketDTO detalleTicketDTO = new DetalleTicketDTO(optionalTicket.get());

        if (!esSoporteOAdmin && !Objects.equals(detalleTicketDTO.getTicket().getUsuario().getId(), usuarioLogeado.getId())) {
            return "<p>No se encontró un ticket asociado a tu cuenta con ese código.</p>";
        }

        String url;
        switch (detalleTicketDTO.getTicket().getFaseTicket().getId().intValue()) {
            case 1 -> url = esSoporteOAdmin ? "/soporte/Recepcionar" : "/inicio";
            case 2 -> url = esSoporteOAdmin ? "/soporte/Atender" : "/TicketsEnProceso";
            case 3 -> url = esSoporteOAdmin ? "/soporte/Tickets-Cerrados" : "/TicketsAtendidos";
            case 4 -> url = esSoporteOAdmin ? "/soporte/Tickets-Desestimados" : "/TicketsDesestimados";
            default -> url = "/inicio";
        }

        // Generar HTML
        StringBuilder html = new StringBuilder();
        html.append("<div>")
                .append("<p><strong>ID:</strong> ").append(detalleTicketDTO.getTicket().getCodigoTicket()).append("</p>")
                .append("<p><strong>Descripción:</strong> ").append(detalleTicketDTO.getTicket().getDescripcion()).append("</p>")
                .append("<p><strong>Usuario:</strong> ").append(detalleTicketDTO.getTicket().getUsuario().getNombre()).append("</p>")
                .append("<p><strong>Fecha:</strong> ").append(detalleTicketDTO.getFechaFormateadaTicket()).append("</p>")
                .append("<p><strong>Hora:</strong> ").append(detalleTicketDTO.getHoraFormateadaTicket()).append("</p>")
                .append("<p><strong>Estado:</strong> ").append(detalleTicketDTO.getTicket().getFaseTicket().getNombre()).append("</p>")
                .append("<a disabled href='").append("/ticket/").append(detalleTicketDTO.getTicket().getCodigoTicket()).append("' class='btn btn-primary' target='_blank'>Ver Ticket</a>")
                .append("</div>");

        return html.toString();
    }




    @GetMapping("/datos/{codigoTicket}")
    public ResponseEntity<?> obtenerDatosTicket(@PathVariable String codigoTicket) {
        Ticket ticket = ticketRepository.findByCodigoTicket(codigoTicket)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket no encontrado"));

        DetalleTicketDTO detalleTicketDTO = new DetalleTicketDTO(ticket);

        int idFase = detalleTicketDTO.getTicket().getFaseTicket().getId().intValue();
        switch (idFase) {
            case 1 -> {
                TicketRecordWS record = new TicketRecordWS(detalleTicketDTO);
                return ResponseEntity.ok(record);
            }
            case 2 -> {
                RecepcionRecordWS record = new RecepcionRecordWS(detalleTicketDTO);
                return ResponseEntity.ok(record);
            }
            case 3 -> {
                AtencionRecordWS record = new AtencionRecordWS(detalleTicketDTO);
                return ResponseEntity.ok(record);
            }
            case 4 -> {
                DesestimacionRecordWS record = new DesestimacionRecordWS(detalleTicketDTO);
                return ResponseEntity.ok(record);
            }
            default -> {
                return ResponseEntity.badRequest().body("Fase de ticket no soportada para notificación.");
            }
        }
    }






    //crea ticket y además lo envia a través de webSocket para notificaciones y actualizaciones en tiempo real
    @PostMapping("/crearTicket")
    public ResponseEntity<String> crearTicket(
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo,
            HttpServletResponse response) throws IOException {

        // Lógica para crear el ticket
        Ticket ticket = new Ticket();

        Integer ultimoNumero = ticketRepository.obtenerUltimoNumeroTicket();
        if (ultimoNumero == null) {
            ultimoNumero = 0;
        }
        String codigo = String.format("TK-%04d", ultimoNumero + 1);
        ticket.setCodigoTicket(codigo);


        HorarioAtencionSoporte ultimoHorarioAtencionSoporte = horarioAtencionSoporteRepository.findTopByOrderByIdDesc();

        ticket.setCodigoTicket(codigo);
        ticket.setDescripcion(descripcion);
        ticket.setFaseTicket(ticketService.getFaseTicketPorID(1L)); // Enviado
        ticket.setUsuario(usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado()));
        ticket.setHorarioAtencionSoporte(ultimoHorarioAtencionSoporte);

        LocalTime horaCreacionTicket = LocalTime.now();

        if (ultimoHorarioAtencionSoporte != null) {
            LocalTime horaEntrada = ultimoHorarioAtencionSoporte.getHoraEntrada();
            LocalTime horaSalida = ultimoHorarioAtencionSoporte.getHoraSalida();

            if (horaCreacionTicket.isBefore(horaEntrada) || horaCreacionTicket.isAfter(horaSalida)) {
                System.out.println("⚠ El ticket fue creado FUERA del horario de atención: " + horaCreacionTicket);

                DetalleEnEspera detalleEnEspera = new DetalleEnEspera();
                detalleEnEspera.setHoraInicio(horaCreacionTicket);
                detalleEnEspera.setFechaInicio(LocalDate.now());
                detalleEnEspera.setClasificacionEspera(clasificacionEsperaRepository.findById(1L).orElseThrow());
                detalleEnEspera.setTicket(ticket);
                detalleEnEspera.setDescripcion("El ticket ha sido puesto en espera automáticamente porque fue enviado fuera del horario de atención establecido. Será atendido en el siguiente horario laboral.");
                detalleEnEspera.setUsuario(ticket.getUsuario());

                // Lógica para determinar la fechaFin
                LocalDate fechaFin;
                if (horaCreacionTicket.isBefore(horaEntrada)) {
                    // El ticket se creó antes del horario laboral de hoy
                    fechaFin = LocalDate.now();
                } else {
                    // El ticket se creó después del horario laboral de hoy
                    fechaFin = LocalDate.now().plusDays(1);
                }

                detalleEnEspera.setFechaFin(fechaFin);
                detalleEnEspera.setHoraFin(horaEntrada);
                ticketService.saveTicket(ticket);
                detalleEnEsperaRepository.save(detalleEnEspera);
                System.out.println("ℹ DetalleEnEspera guardado automáticamente hasta " + fechaFin + " " + horaEntrada);
            }
        }


        ticketService.saveTicket(ticket);





        List<ArchivoAdjuntoEnvio> listaArchivosAdjuntos = new ArrayList<>();
        // Si el archivo no es nulo y no está vacío, guardarlo
        if (archivo != null && !archivo.isEmpty()) {
            try {
                ArchivoAdjuntoEnvio archivoAdjuntoEnvio = new ArchivoAdjuntoEnvio();
                archivoAdjuntoEnvio.setNombre(archivo.getOriginalFilename());
                archivoAdjuntoEnvio.setArchivo(archivo.getBytes());  // Convertir archivo a bytes
                archivoAdjuntoEnvio.setTipoContenido(archivo.getContentType());
                archivoAdjuntoEnvio.setPesoContenido((double) archivo.getSize() / 1024); // Tamaño en KB
                archivoAdjuntoEnvio.setTicket(ticket);  // Asignar el ticket recién creado
                listaArchivosAdjuntos.add(archivoAdjuntoEnvio);
                // Guardar el archivo en la base de datos
                ticketService.saveAdjuntoEnvio(archivoAdjuntoEnvio);
                ticket.setListaArchivosAdjuntos(listaArchivosAdjuntos);

            } catch (IOException e) {
                ResponseEntity.badRequest().body("Error al procesar el archivo");
                System.out.println(e.getMessage());
                System.out.println(Arrays.toString(e.getStackTrace()));
                throw new RuntimeException(e);
            }

        }


        //NOTIFICACION EN BASE DE DATOS
        List<Usuario> listaSoportes = usuarioService.ListaUsuariosPorRol(2L);
        for (Usuario soporte : listaSoportes) {
            Notificacion notificacion = new Notificacion();
            notificacion.setTicket(ticket);
            notificacion.setHora(notificacion.getHora());
            notificacion.setFecha(notificacion.getFecha());
            notificacion.setAbierto(Boolean.FALSE);
            notificacion.setLeido(Boolean.FALSE);
            notificacion.setUsuario(soporte);
            notificacion.setMensaje(ticket.getUsuario().getNombre() + " Ha Enviado un Ticket");
            notificacion.setUrl("/ticket/"+ticket.getCodigoTicket());
            notificacionesService.saveNotiicacion(notificacion);
            //AUMENTA EL CONTADOR DE NOTIFICACIONES EN TIEMPO REAL A LOS DE SOPORTE
            WSNotificacionesService.enviarNotificacion(notificacion);
        }
        List<Usuario> listaAdmins = usuarioService.ListaUsuariosPorRol(3L);
        for (Usuario admin : listaAdmins) {
            Notificacion notificacion = new Notificacion();
            notificacion.setTicket(ticket);
            notificacion.setHora(notificacion.getHora());
            notificacion.setFecha(notificacion.getFecha());
            notificacion.setAbierto(Boolean.FALSE);
            notificacion.setLeido(Boolean.FALSE);
            notificacion.setUsuario(admin);
            notificacion.setMensaje(ticket.getUsuario().getNombre() + " Ha Enviado un Ticket");
            notificacion.setUrl("/ticket/"+ticket.getCodigoTicket());
            notificacionesService.saveNotiicacion(notificacion);
            //AUMENTA EL CONTADOR DE NOTIFICACIONES EN TIEMPO REAL A LOS ADMIN
            WSNotificacionesService.enviarNotificacion(notificacion);
        }

        //NOTIFICACIONES EN TIEMPO REAL A TRAVES DE WEB SOCKETS
        DetalleTicketDTO ticketDTO = new DetalleTicketDTO(ticket);
        WSNotificacionesService.enviarTicketAVistaSoporteRecepcion(ticketDTO);
        WSNotificacionesService.enviarTicketAVistaEnviadosUsuario(ticketDTO);
        WSNotificacionesService.notificarActualizacionDashboard();
        response.sendRedirect("/ticket/" + ticket.getCodigoTicket());
        return ResponseEntity.ok("Ticket creado correctamente");
    }


    @GetMapping("/adjuntoEnvio/descargar/{id}")
    public ResponseEntity<Resource> descargarArchivoEnvio(@PathVariable Long id) {
        ArchivoAdjuntoEnvio archivoAdjuntoEnvio = ticketService.getArchivoEnvioPorId(id);
        Resource recurso = new ByteArrayResource(archivoAdjuntoEnvio.getArchivo());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(archivoAdjuntoEnvio.getTipoContenido())) // Tipo MIME
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivoAdjuntoEnvio.getNombre() + "\"") // Para descarga
                .body(recurso); // El cuerpo de la respuesta es el recurso
    }
    @GetMapping("/adjuntoAtencion/descargar/{id}")
    public ResponseEntity<Resource> descargarArchivoAtencion(@PathVariable Long id) {
        ArchivoAdjuntoAtencion archivoAdjuntoAtencion = ticketService.getArchivoAtencionPorId(id);
        Resource recurso = new ByteArrayResource(archivoAdjuntoAtencion.getArchivo());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(archivoAdjuntoAtencion.getTipoContenido()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivoAdjuntoAtencion.getNombre() + "\"")
                .body(recurso);
    }
    @GetMapping("/adjuntoDesestimacion/descargar/{id}")
    public ResponseEntity<Resource> descargarArchivoDesestimacion(@PathVariable Long id) {
        ArchivoAdjuntoDesestimacion archivoAdjuntoDesestimacion = ticketService.getArchivoDesestimacionPorId(id);
        Resource recurso = new ByteArrayResource(archivoAdjuntoDesestimacion.getArchivo());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(archivoAdjuntoDesestimacion.getTipoContenido()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivoAdjuntoDesestimacion.getNombre() + "\"")
                .body(recurso);
    }
    @GetMapping("/adjuntoEspera/descargar/{id}")
    public ResponseEntity<Resource> descargarArchivoEspera(@PathVariable Long id) {
        ArchivoAdjuntoEspera archivoAdjuntoEspera = ticketService.getArchivoEsperaPorId(id);
        Resource recurso = new ByteArrayResource(archivoAdjuntoEspera.getArchivo());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(archivoAdjuntoEspera.getTipoContenido()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivoAdjuntoEspera.getNombre() + "\"")
                .body(recurso);
    }

}
