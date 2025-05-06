package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.WebSocket.WSNotificacionesService;
import com.ipor.ticketsystem.model.dto.DetalleTicketDTO;
import com.ipor.ticketsystem.model.dynamic.*;
import com.ipor.ticketsystem.repository.dynamic.TicketRepository;
import com.ipor.ticketsystem.service.NotificacionesService;
import com.ipor.ticketsystem.service.TicketService;
import com.ipor.ticketsystem.service.UsuarioService;
import org.springframework.core.io.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/app/tickets")
public class TicketController {
    @Autowired
    TicketService ticketService;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    WSNotificacionesService WSNotificacionesService;
    @Autowired
    NotificacionesService notificacionesService;


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



    //crea ticket y además lo envia a través de webSocket para notificaciones y actualizaciones en tiempo real
    @PostMapping("/crearTicket")
    public ResponseEntity<String> crearTicket(
            @RequestParam("descripcion") String descripcion,
//            @RequestParam("clasificacion") Long clasificacion,
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


        ticket.setCodigoTicket(codigo);
        ticket.setDescripcion(descripcion);
        ticket.setFaseTicket(ticketService.getFaseTicketPorID(1L)); //enviado
        ticket.setUsuario(usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado()));
        ticket.setFecha(ticket.getFecha());
        ticket.setHora(ticket.getHora());

        ticketService.saveTicket(ticket);


        List<ArchivoAdjunto> listaArchivosAdjuntos = new ArrayList<>();
        // Si el archivo no es nulo y no está vacío, guardarlo
        if (archivo != null && !archivo.isEmpty()) {
            try {
                ArchivoAdjunto archivoAdjunto = new ArchivoAdjunto();
                archivoAdjunto.setNombre(archivo.getOriginalFilename());
                archivoAdjunto.setArchivo(archivo.getBytes());  // Convertir archivo a bytes
                archivoAdjunto.setTipoContenido(archivo.getContentType());
                archivoAdjunto.setPesoContenido((double) archivo.getSize() / 1024); // Tamaño en KB
                archivoAdjunto.setTicket(ticket);  // Asignar el ticket recién creado
                listaArchivosAdjuntos.add(archivoAdjunto);
                // Guardar el archivo en la base de datos
                ticketService.saveAdjunto(archivoAdjunto);
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

        //NOTIFICACIONES EN TIEMPO REAL A TRAVES DE WEB SOCKETS
        DetalleTicketDTO ticketDTO = new DetalleTicketDTO(ticket);
        WSNotificacionesService.enviarTicketAVistaSoporteRecepcion(ticketDTO);
        WSNotificacionesService.enviarTicketAVistaEnviadosUsuario(ticketDTO);
        WSNotificacionesService.notificarActualizacionDashboard();

        response.sendRedirect("/inicio");
        return ResponseEntity.ok("Ticket creado correctamente");
    }


    @GetMapping("/adjunto/descargar/{id}")
    public ResponseEntity<Resource> descargarArchivo(@PathVariable Long id) {
        // Obtener el archivo adjunto por su ID desde el servicio
        ArchivoAdjunto archivoAdjunto = ticketService.getArchivoPorId(id);

        // Crear un recurso basado en los bytes del archivo
        Resource recurso = new ByteArrayResource(archivoAdjunto.getArchivo());

        // Devolver el archivo como respuesta, con el tipo de contenido adecuado
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(archivoAdjunto.getTipoContenido())) // Tipo MIME
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivoAdjunto.getNombre() + "\"") // Para descarga
                .body(recurso); // El cuerpo de la respuesta es el recurso
    }

}
