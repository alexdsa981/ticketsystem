package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.WebSocket.WSNotificacionesService;
import com.ipor.ticketsystem.model.dto.DetalleTicketDTO;
import com.ipor.ticketsystem.model.dynamic.*;
import com.ipor.ticketsystem.model.fixed.HorarioAtencionSoporte;
import com.ipor.ticketsystem.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/app/tickets")
public class AtencionController {
    @Autowired
    AtencionService atencionService;
    @Autowired
    TicketService ticketService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ClasificadoresService clasificadoresService;
    @Autowired
    NotificacionesService notificacionesService;
    @Autowired
    WSNotificacionesService WSNotificacionesService;

    //ESTA CLASE REPRESENTA LA LOGICA PARA MOSTRAR TICKETS DESDE LA FASE RECEPCIÓN
    // ATENDIDO O DESESTIMADO EN LAS VISTAS

    public Model getListaMisTicketsRecepcionadosAVista(Model model) {
        List<DetalleTicketDTO> MyRecepcionados = atencionService.getMyListaRecepcionados();
        model.addAttribute("MyRecepcionados", MyRecepcionados);
        return model;
    }

    public Model getListaTodosLosTicketsRecepcionadosAVista(Model model) {
        List<DetalleTicketDTO> AllRecepcionados = atencionService.getListaRecepcionados();
        model.addAttribute("AllRecepcionados", AllRecepcionados);
        return model;
    }


    public Model getListaMisTicketsAtendidosAVista(Model model) {
        List<DetalleTicketDTO> MyAtendidos = atencionService.getMyListaAtendidos();
        model.addAttribute("MyAtendidos", MyAtendidos);
        return model;
    }

    public Model getListaTodosLosTicketsAtendidosAVista(Model model) {
        List<DetalleTicketDTO> AllAtendidos = atencionService.getListaHistorialAtencion();
        model.addAttribute("AllAtendidos", AllAtendidos);
        return model;
    }

    public Model getListaMisTicketsDesestimadosAVista(Model model) {
        List<DetalleTicketDTO> MyDesestimados = atencionService.getMyListaDesestimados();
        model.addAttribute("MyDesestimados", MyDesestimados);
        return model;
    }

    public Model getListaTodosLosTicketsDesestimadosAVista(Model model) {
        List<DetalleTicketDTO> AllDesestimados = atencionService.getListaDesestimados();
        model.addAttribute("AllDesestimados", AllDesestimados);
        return model;
    }


    public Model getListaMisTicketsEnEsperaAVista(Model model) {
        List<DetalleTicketDTO> MyEnEspera = atencionService.getMyListaEspera();
        model.addAttribute("MyEnEspera", MyEnEspera);
        return model;
    }

    public Model getListaTodosLosTicketsEnEsperaAVista(Model model) {
        List<DetalleTicketDTO> AllEnEspera = atencionService.getListaEspera();
        model.addAttribute("AllEnEspera", AllEnEspera);
        return model;
    }


    //metodo para recepcionar un ticket(desde soporte), crea un recepcionado y cambia la fase del ticket:
    @PostMapping("/recepcion/{id}")
    public ResponseEntity<String> recepcionarTicket(
            @RequestParam("mensaje") String mensaje,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        try {
            // Cambiar fase de ticket
            atencionService.updateFaseTicket(id, 2L);

            Ticket ticket = ticketService.getTicketPorID(id);


            Recepcion recepcion = new Recepcion();
            recepcion.setMensaje(mensaje);
            recepcion.setTicket(ticket);
            recepcion.setUsuario(usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado()));
            atencionService.saveRecepcion(recepcion);

            ticket.setRecepcion(recepcion);
            ticketService.saveTicket(ticket);

            HorarioAtencionSoporte horarioAtencionSoporte = clasificadoresService.getLastHorarioAtencionSoporte();

            if (ticket.getListaDetalleEsperas() != null && !ticket.getListaDetalleEsperas().isEmpty()) {
                ticket.getListaDetalleEsperas().stream()
                        .filter(e -> e.getFechaFin() == null || e.getHoraFin() == null || e.getHoraFin() == horarioAtencionSoporte.getHoraEntrada())
                        .forEach(detalle -> {
                            detalle.setFechaFin(LocalDate.now());
                            detalle.setHoraFin(LocalTime.now());
                            atencionService.saveEspera(detalle);
                            System.out.println("⚠ detalle espera por fuera de horario terminado: " + detalle.getId());
                        });
            }


            // Crear notificación
            Notificacion notificacion = new Notificacion();
            notificacion.setTicket(recepcion.getTicket());

            notificacion.setAbierto(Boolean.FALSE);
            notificacion.setLeido(Boolean.FALSE);
            notificacion.setUsuario(recepcion.getTicket().getUsuario());
            notificacion.setMensaje(" Ha sido Recepcionado");
            notificacion.setUrl("/ticket/" + ticket.getCodigoTicket());
            notificacionesService.saveNotiicacion(notificacion);
            WSNotificacionesService.enviarNotificacion(notificacion);
            WSNotificacionesService.ocultarRegistroEnVistaSoporteRecepcion(id);
            WSNotificacionesService.enviarRecepcionAVistaSoporteAtencion(ticket);
            WSNotificacionesService.enviarRecepcionAVistaUsuarioRecepcionados(ticket);
            WSNotificacionesService.ocultarRegistroEnVistaEnviadosUsuario(id);
            WSNotificacionesService.notificarActualizacionDashboard();
            WSNotificacionesService.notificarActualizacionPaginaTicket(ticket);

            // Redirigir a la URL actual
            String referer = request.getHeader("Referer");
            String redirectUrl = (referer != null ? referer : "/fallbackUrl") + "?successful=atencion";
            response.sendRedirect(redirectUrl);
            return ResponseEntity.ok("Ticket recepcionado correctamente");

        } catch (DataIntegrityViolationException e) {
            // Error de llave duplicada u otros problemas de integridad
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer + "?error=recepcion-duplicated" : "/fallbackUrl?error=recepcion-duplicated");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: El ticket ya ha sido recepcionado por otro usuario.");
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String fullStackTrace = sw.toString();

            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer + "?error=recepcion-general" : "/fallbackUrl?recepcion-error=general");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al recepcionar el ticket.");
        }
    }


    //metodo para atender un ticket, crea una atencion y cambia la fase del ticket:
    @PostMapping("/atencion/{id}")
    public ResponseEntity<String> atenderTicket(
            @RequestParam("descripcion") String descripcion,
            @RequestParam("area_atencion") Long IDarea_atencion,
            @RequestParam("clasificacion_urgencia") Long IDclasificacion_urgencia,
            @RequestParam("tipo_incidencia") Long ID_tipo_incidencia,
            @RequestParam("clasificacion_atencion") Long IDclasificacion_atencion,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            //cambiar fase de ticket
            atencionService.updateFaseTicket(id, 3L);

            Ticket ticket = ticketService.getTicketPorID(id);
            // Lógica para crear la atencion
            Atencion atencion = new Atencion();
            atencion.setDescripcion(descripcion);
            atencion.setClasificacionAtencion(clasificadoresService.getClasificacionAtencionPorId(IDclasificacion_atencion));
            atencion.setClasificacionUrgencia(clasificadoresService.getClasificacionUrgenciaPorId(IDclasificacion_urgencia));
            atencion.setTipoIncidencia(clasificadoresService.getTipoIncidenciaPorID(ID_tipo_incidencia));
            atencion.setAreaAtencion(clasificadoresService.getAreaAtencionPorId(IDarea_atencion));
            atencion.setTicket(ticket);
            atencion.setUsuario(usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado()));
            atencionService.saveAtencion(atencion);

            // Modificar el ticket
            ticket.setAtencion(atencion);
            ticketService.saveTicket(ticket);



            if (ticket.getListaDetalleEsperas() != null && !ticket.getListaDetalleEsperas().isEmpty()) {
                ticket.getListaDetalleEsperas().stream()
                        .filter(e -> e.getFechaFin() == null || e.getHoraFin() == null)
                        .forEach(detalle -> {
                            detalle.setFechaFin(LocalDate.now());
                            detalle.setHoraFin(LocalTime.now());
                            atencionService.saveEspera(detalle);
                        });
            }

            List<ArchivoAdjuntoAtencion> listaArchivosAdjuntos = new ArrayList<>();
            // Si el archivo no es nulo y no está vacío, guardarlo
            if (archivo != null && !archivo.isEmpty()) {
                try {
                    ArchivoAdjuntoAtencion archivoAdjuntoAtencion = new ArchivoAdjuntoAtencion();
                    archivoAdjuntoAtencion.setNombre(archivo.getOriginalFilename());
                    archivoAdjuntoAtencion.setArchivo(archivo.getBytes());  // Convertir archivo a bytes
                    archivoAdjuntoAtencion.setTipoContenido(archivo.getContentType());
                    archivoAdjuntoAtencion.setPesoContenido((double) archivo.getSize() / 1024); // Tamaño en KB
                    archivoAdjuntoAtencion.setAtencion(atencion);  // Asignar el ticket recién creado
                    listaArchivosAdjuntos.add(archivoAdjuntoAtencion);
                    // Guardar el archivo en la base de datos
                    ticketService.saveAdjuntoAtencion(archivoAdjuntoAtencion);
                    atencion.setListaArchivosAdjuntos(listaArchivosAdjuntos);

                } catch (IOException e) {
                    ResponseEntity.badRequest().body("Error al procesar el archivo");
                    System.out.println(e.getMessage());
                    System.out.println(Arrays.toString(e.getStackTrace()));
                    throw new RuntimeException(e);
                }

            }


            Notificacion notificacion = new Notificacion();
            notificacion.setTicket(atencion.getTicket());
            notificacion.setAbierto(Boolean.FALSE);
            notificacion.setLeido(Boolean.FALSE);
            notificacion.setUsuario(atencion.getTicket().getUsuario());
            notificacion.setMensaje(" Ha sido Atendido");
            notificacion.setUrl("/ticket/" + ticket.getCodigoTicket());
            notificacionesService.saveNotiicacion(notificacion);
            WSNotificacionesService.enviarNotificacion(notificacion);
            WSNotificacionesService.ocultarRegistroEnVistaSoporteAtencion(id);
            WSNotificacionesService.ocultarRegistroEnVistaUsuarioRecepcionados(id);
            WSNotificacionesService.enviarAtencionAVistaSoporteHistorialAtencion(ticket);
            WSNotificacionesService.enviarAtencionAVistaUsuarioAtendidos(ticket);
            WSNotificacionesService.notificarActualizacionDashboard();
            WSNotificacionesService.notificarActualizacionPaginaTicket(ticket);

            // Redirigir a la URL actual
            String referer = request.getHeader("Referer");
            String redirectUrl = (referer != null ? referer : "/fallbackUrl") + "?successful=atencion";
            response.sendRedirect(redirectUrl);
            return ResponseEntity.ok("Ticket atendido correctamente");
        } catch (DataIntegrityViolationException e) {
            // Error de llave duplicada u otros problemas de integridad
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer + "?error=atencion-duplicated" : "/fallbackUrl?error=atencion-duplicated");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: El ticket ya ha sido atendido por otro usuario.");
        } catch (Exception e) {
            // Captura otros errores
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer + "?error=atencion-general" : "/fallbackUrl?error=atencion-general");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al atender el ticket.");
        }

    }


    @PostMapping("/espera/{id}")
    public ResponseEntity<String> esperaTicket(
            @RequestParam("mensaje") String mensaje,
            @RequestParam("fase") int fase,
            @RequestParam("clasificacion_espera") Long IDclasificacion_espera,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            Ticket ticket = ticketService.getTicketPorID(id);

            Long lastFaseTicket = ticket.getFaseTicket().getId();

            if (fase != lastFaseTicket) {
                String referer = request.getHeader("Referer");
                String redirectUrl = (referer != null ? referer : "/fallbackUrl") + "?error=espera-moved";
                response.sendRedirect(redirectUrl);
                return ResponseEntity.ok("Ticket ya fue movido a otra fase.");
            }


            // Cambiar fase del ticket
            atencionService.updateFaseTicket(id, 5L);

            // Lógica para crear la espera
            DetalleEnEspera espera = new DetalleEnEspera();
            espera.setDescripcion(mensaje);
            espera.setClasificacionEspera(clasificadoresService.getClasificacionEsperaPorId(IDclasificacion_espera));
            espera.setTicket(ticket);
            espera.setUsuario(usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado()));
            espera.setFechaInicio(LocalDate.now());
            espera.setHoraInicio(LocalTime.now());
            atencionService.saveEspera(espera);

            List<DetalleEnEspera> listaEsperas = new ArrayList<>();
            listaEsperas.add(espera);

            // Modificar el ticket
            ticket.setListaDetalleEsperas(listaEsperas);
            ticketService.saveTicket(ticket);



            List<ArchivoAdjuntoEspera> listaArchivosAdjuntos = new ArrayList<>();
            // Si el archivo no es nulo y no está vacío, guardarlo
            if (archivo != null && !archivo.isEmpty()) {
                try {
                    ArchivoAdjuntoEspera archivoAdjuntoEspera = new ArchivoAdjuntoEspera();
                    archivoAdjuntoEspera.setNombre(archivo.getOriginalFilename());
                    archivoAdjuntoEspera.setArchivo(archivo.getBytes());  // Convertir archivo a bytes
                    archivoAdjuntoEspera.setTipoContenido(archivo.getContentType());
                    archivoAdjuntoEspera.setPesoContenido((double) archivo.getSize() / 1024); // Tamaño en KB
                    archivoAdjuntoEspera.setDetalleEnEspera(espera);  // Asignar el ticket recién creado
                    listaArchivosAdjuntos.add(archivoAdjuntoEspera);
                    // Guardar el archivo en la base de datos
                    ticketService.saveAdjuntoEspera(archivoAdjuntoEspera);
                    espera.setListaArchivosAdjuntos(listaArchivosAdjuntos);

                } catch (IOException e) {
                    ResponseEntity.badRequest().body("Error al procesar el archivo");
                    System.out.println(e.getMessage());
                    System.out.println(Arrays.toString(e.getStackTrace()));
                    throw new RuntimeException(e);
                }

            }


            Notificacion notificacion = new Notificacion();
            notificacion.setTicket(espera.getTicket());
            notificacion.setAbierto(Boolean.FALSE);
            notificacion.setLeido(Boolean.FALSE);
            notificacion.setUsuario(espera.getTicket().getUsuario());
            notificacion.setMensaje(" Ha sido puesto en Espera");
            notificacion.setUrl("/ticket/" + ticket.getCodigoTicket());
            notificacionesService.saveNotiicacion(notificacion);

            WSNotificacionesService.enviarNotificacion(notificacion);
            WSNotificacionesService.enviarTicketAVistaSoporteEnEspera(ticket);
            WSNotificacionesService.enviarTicketAVistaEnEsperaUsuario(ticket);
            WSNotificacionesService.ocultarRegistroEnVistaSoporteAtencion(id);
            WSNotificacionesService.ocultarRegistroEnVistaUsuarioRecepcionados(id);

            WSNotificacionesService.notificarActualizacionDashboard();
            WSNotificacionesService.notificarActualizacionPaginaTicket(ticket);


            String referer = request.getHeader("Referer");
            String redirectUrl = (referer != null ? referer : "/fallbackUrl") + "?successful=espera";
            response.sendRedirect(redirectUrl);
            return ResponseEntity.ok("Ticket puesto en Espera correctamente");


        } catch (DataIntegrityViolationException e) {
            // Error de llave duplicada u otros problemas de integridad
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer + "?error=espera-duplicated" : "/fallbackUrl?error=espera-duplicated");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: El ticket ya ha sido puesto en Espera por otro usuario.");
        } catch (Exception e) {
            e.printStackTrace();
            ;
            // Captura otros errores
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer + "?error=espera-general" : "/fallbackUrl?error=espera-general");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al poner en Espera el ticket.");
        }

    }


    // Método para desestimar un ticket, crea una desestimación y cambia la fase del ticket:
    @PostMapping("/desestimacion/{id}")
    public ResponseEntity<String> desestimarTicket(
            @RequestParam("mensaje") String mensaje,
            @RequestParam("fase") int fase,
            @RequestParam("clasificacion_desestimacion") Long IDclasificacion_desestimacion,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo,
            @PathVariable Long id,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            Ticket ticket = ticketService.getTicketPorID(id);

            Long lastFaseTicket = ticket.getFaseTicket().getId();

            if (fase != lastFaseTicket) {
                String referer = request.getHeader("Referer");
                String redirectUrl = (referer != null ? referer : "/fallbackUrl") + "?error=desestimacion-moved";
                response.sendRedirect(redirectUrl);
                return ResponseEntity.ok("Ticket ya fue movido a otra fase.");
            }


            // Cambiar fase del ticket
            atencionService.updateFaseTicket(id, 4L);

            // Lógica para crear la desestimación
            Desestimacion desestimacion = new Desestimacion();
            desestimacion.setDescripcion(mensaje);
            desestimacion.setClasificacionDesestimacion(clasificadoresService.getClasificacionDesestimacionPorId(IDclasificacion_desestimacion));
            desestimacion.setTicket(ticket);
            desestimacion.setUsuario(usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado()));
            atencionService.saveDesestimacion(desestimacion);

            // Modificar el ticket
            ticket.setDesestimacion(desestimacion);
            ticketService.saveTicket(ticket);


            if (ticket.getListaDetalleEsperas() != null && !ticket.getListaDetalleEsperas().isEmpty()) {
                ticket.getListaDetalleEsperas().stream()
                        .filter(e -> e.getFechaFin() == null || e.getHoraFin() == null)
                        .forEach(detalle -> {
                            detalle.setFechaFin(LocalDate.now());
                            detalle.setHoraFin(LocalTime.now());
                            atencionService.saveEspera(detalle);
                        });
            }


            List<ArchivoAdjuntoDesestimacion> listaArchivosAdjuntos = new ArrayList<>();
            // Si el archivo no es nulo y no está vacío, guardarlo
            if (archivo != null && !archivo.isEmpty()) {
                try {
                    ArchivoAdjuntoDesestimacion archivoAdjuntoDesestimacion = new ArchivoAdjuntoDesestimacion();
                    archivoAdjuntoDesestimacion.setNombre(archivo.getOriginalFilename());
                    archivoAdjuntoDesestimacion.setArchivo(archivo.getBytes());  // Convertir archivo a bytes
                    archivoAdjuntoDesestimacion.setTipoContenido(archivo.getContentType());
                    archivoAdjuntoDesestimacion.setPesoContenido((double) archivo.getSize() / 1024); // Tamaño en KB
                    archivoAdjuntoDesestimacion.setDesestimacion(desestimacion);  // Asignar el ticket recién creado
                    listaArchivosAdjuntos.add(archivoAdjuntoDesestimacion);
                    // Guardar el archivo en la base de datos
                    ticketService.saveAdjuntoDesestimacion(archivoAdjuntoDesestimacion);
                    desestimacion.setListaArchivosAdjuntos(listaArchivosAdjuntos);

                } catch (IOException e) {
                    ResponseEntity.badRequest().body("Error al procesar el archivo");
                    System.out.println(e.getMessage());
                    System.out.println(Arrays.toString(e.getStackTrace()));
                    throw new RuntimeException(e);
                }

            }


            Notificacion notificacion = new Notificacion();
            notificacion.setTicket(desestimacion.getTicket());
            notificacion.setAbierto(Boolean.FALSE);
            notificacion.setLeido(Boolean.FALSE);
            notificacion.setUsuario(desestimacion.getTicket().getUsuario());
            notificacion.setMensaje(" Ha sido Desestimado");
            notificacion.setUrl("/ticket/" + ticket.getCodigoTicket());

            notificacionesService.saveNotiicacion(notificacion);
            WSNotificacionesService.enviarNotificacion(notificacion);
            WSNotificacionesService.enviarAtencionAVistaSoporteHistorialDesestimacion(ticket);
            WSNotificacionesService.enviarAtencionAVistaUsuarioDesestimados(ticket);
            if (lastFaseTicket == 1L) {
                WSNotificacionesService.ocultarRegistroEnVistaSoporteRecepcion(id);
            } else if (lastFaseTicket == 2L) {
                WSNotificacionesService.ocultarRegistroEnVistaSoporteAtencion(id);
            } else if (lastFaseTicket == 5L) {
                WSNotificacionesService.ocultarRegistroEnVistaSoporteEnEspera(id);
            }
            WSNotificacionesService.ocultarRegistroEnVistaUsuarioRecepcionados(id);
            WSNotificacionesService.ocultarRegistroEnVistaEnviadosUsuario(id);
            WSNotificacionesService.ocultarRegistroEnVistaEnEsperaUsuario(id);

            if (atencionService.findRecepcionByTicketID(id) != null) {
                atencionService.deleteRecepcion(atencionService.findRecepcionByTicketID(id));
            }
            WSNotificacionesService.notificarActualizacionDashboard();
            WSNotificacionesService.notificarActualizacionPaginaTicket(ticket);


            String referer = request.getHeader("Referer");
            String redirectUrl = (referer != null ? referer : "/fallbackUrl") + "?successful=desestimacion";
            response.sendRedirect(redirectUrl);
            return ResponseEntity.ok("Ticket desestimado correctamente");


        } catch (DataIntegrityViolationException e) {
            // Error de llave duplicada u otros problemas de integridad
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer + "?error=desestimacion-duplicated" : "/fallbackUrl?error=desestimacion-duplicated");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: El ticket ya ha sido desestimado por otro usuario.");
        } catch (Exception e) {
            // Captura otros errores
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer + "?error=desestimacion-general" : "/fallbackUrl?error=desestimacion-general");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al desestimar el ticket.");
        }

    }


}
