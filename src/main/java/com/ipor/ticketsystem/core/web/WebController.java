package com.ipor.ticketsystem.core.web;

import com.ipor.ticketsystem.ticket.dto.DetalleTicketDTO;
import com.ipor.ticketsystem.ticket.DetalleEnEspera;
import com.ipor.ticketsystem.ticket.Ticket;
import com.ipor.ticketsystem.ticket.controller.AtencionController;
import com.ipor.ticketsystem.ticket.controller.ClasificadoresController;
import com.ipor.ticketsystem.ticket.controller.TicketController;
import com.ipor.ticketsystem.usuario.*;
import com.ipor.ticketsystem.ticket.comun.HorarioAtencionSoporte;
import com.ipor.ticketsystem.ticket.repository.RecepcionRepository;
import com.ipor.ticketsystem.ticket.repository.AtencionRepository;
import com.ipor.ticketsystem.ticket.repository.TicketRepository;
import com.ipor.ticketsystem.ticket.comun.HorarioAtencionSoporteRepository;
import com.ipor.ticketsystem.ticket.service.TicketService;
import com.ipor.ticketsystem.usuario.login.LoginController;
import com.ipor.ticketsystem.usuario.rol.RolUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class WebController {
    @Autowired
    private TicketController ticketController;
    @Autowired
    private AtencionController atencionController;
    @Autowired
    private UsuariosController usuariosController;
    @Autowired
    private ClasificadoresController clasificadoresController;
    @Autowired
    private LoginController loginController;


    @Autowired
    HorarioAtencionSoporteRepository horarioAtencionSoporteRepository;
    @Autowired
    RecepcionRepository recepcionRepository;
    @Autowired
    AtencionRepository atencionRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UsuarioService usuarioService;

    //redirige / a /login
    @GetMapping("/")
    public String redirectToInicio() {
        return "redirect:/login";
    }

    //abre index.html en /login
    @GetMapping("/login")
    public String redirigePaginaLogin(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "username", required = false) String username,
            Model model) {
        // Obtén la autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Si el usuario está autenticado y no es anónimo, redirige a la página de inicio
        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/inicio"; // Redirige a la vista de inicio
        }

        // Añade los atributos para la vista
        model.addAttribute("error", error);
        model.addAttribute("username", username);

        // Si no está autenticado, redirige a la vista de login
        return "index";
    }


    // Método para manejar la vista de inicio y mostrar los tickets
    @GetMapping("/inicio")
    public String redirigePaginaInicio(Model model) {
        ticketController.retornaTicketsPropiosAVista(model);
        model.addAttribute("Titulo", "HelpDesk | Inicio");
        return "general/inicio"; // Redirige a la vista 'inicio.html'
    }

    @GetMapping("/TicketsEnProceso")
    public String redirigePaginaTicketsEnProceso(Model model) {
        atencionController.getListaMisTicketsRecepcionadosAVista(model);
        model.addAttribute("Titulo", "HelpDesk | En Proceso");
        return "general/enProceso";
    }


    @GetMapping("/TicketsEnEspera")
    public String redirigePaginaTicketsEnEspera(Model model) {
        atencionController.getListaMisTicketsEnEsperaAVista(model);
        model.addAttribute("Titulo", "HelpDesk | En Espera");
        return "general/enEspera";
    }

    @GetMapping("/TicketsAtendidos")
    public String redirigePaginaMisTicketsAtendidos(Model model) {
        atencionController.getListaMisTicketsAtendidosAVista(model);
        model.addAttribute("Titulo", "HelpDesk | Atendidos");
        return "general/atendidos";
    }

    @GetMapping("/TicketsDesestimados")
    public String redirigePaginaMisTicketsDesestimados(Model model) {
        atencionController.getListaMisTicketsDesestimadosAVista(model);
        model.addAttribute("Titulo", "HelpDesk | Desestimados");
        return "general/desestimados";
    }



    @GetMapping("/admin/Dashboard")
    public String redirigePaginaDashboardAdmin(Model model) {
        clasificadoresController.getListaClasificacionesUrgenciaActivos(model);
        clasificadoresController.getListaSedesActivos(model);
        clasificadoresController.getListaCatIncidenciaActivos(model);
        model.addAttribute("Titulo", "HelpDesk | Admin - Dashboard");
        return "admin/dashboard";
    }


    @GetMapping("/admin/Usuarios")
    public String redirigePaginaUsuarios(Model model) {
        usuariosController.listarUsuariosDesactivados(model);
        usuariosController.listarUsuariosActivos(model);
        usuariosController.listarRoles(model);
        model.addAttribute("Titulo", "HelpDesk | Admin - Gestion de Usuarios");

        return "admin/usuarios";
    }

    @GetMapping("/admin/Clasificadores")
    public String mostrarClasificadores(
            @RequestParam(name = "clasificador", required = false) String clasificador,
            @RequestParam(name = "categoria", required = false) String categoria,
            @RequestParam(name = "subcategoria", required = false) String subcategoria,
            @RequestParam(name = "sede", required = false) String sede,
            Model model
    ) {
        if (clasificador == null) clasificador = "Area";

        model.addAttribute("clasificadorActivo", clasificador);
        clasificadoresController.listarClasificadores(model, categoria, subcategoria, sede); // NUEVO
        model.addAttribute("Titulo", "HelpDesk | Admin - Gestion de Clasificadores");

        model.addAttribute("categoriaSeleccionada", categoria);
        model.addAttribute("subcategoriaSeleccionada", subcategoria);
        model.addAttribute("sedeSeleccionada", sede); // NUEVO

        return "admin/clasificadores";
    }



    @GetMapping("/soporte/Recepcionar")
    public String redirigePaginaTicketsRecibidos(Model model) {
        ticketController.retornaTicketsRecibidosAVista(model);
        clasificadoresController.getListaClasificacionesUrgenciaActivos(model);
        clasificadoresController.getListaClasificacionesDesestimacionActivos(model);
        clasificadoresController.getListaTipoIncidenciaActivos(model);
        model.addAttribute("Titulo", "HelpDesk | Soporte - Recepcionar Tickets");
        return "soporte/ticketsRecibidos";
    }

    @GetMapping("/soporte/Atender")
    public String redirigePaginaTicketsRecepcionados(Model model) {
        atencionController.getListaTodosLosTicketsRecepcionadosAVista(model);
        clasificadoresController.getListaClasificacionesAtencionActivos(model);
        clasificadoresController.getListaClasificacionesEsperaActivos(model);
        clasificadoresController.getListaTipoIncidenciaActivos(model);
        clasificadoresController.getListaClasificacionesDesestimacionActivos(model);
        clasificadoresController.getListaClasificacionesUrgenciaActivos(model);
        clasificadoresController.getListaSedesActivos(model);
        clasificadoresController.getListaCatIncidenciaActivos(model);
        model.addAttribute("Titulo", "HelpDesk | Soporte - Atender Tickets");
        return "soporte/ticketsRecepcionados";
    }





    @GetMapping("/soporte/Atender-Espera")
    public String redirigePaginaTicketsEnPausa(Model model) {
        atencionController.getListaTodosLosTicketsEnEsperaAVista(model);
        clasificadoresController.getListaClasificacionesAtencionActivos(model);
        clasificadoresController.getListaTipoIncidenciaActivos(model);
        clasificadoresController.getListaClasificacionesDesestimacionActivos(model);
        clasificadoresController.getListaClasificacionesUrgenciaActivos(model);
        clasificadoresController.getListaSedesActivos(model);
        clasificadoresController.getListaCatIncidenciaActivos(model);
            model.addAttribute("Titulo", "HelpDesk | Soporte - Tickets En Espera");
        return "soporte/ticketsEnEspera";
    }







    @GetMapping("/soporte/Tickets-Cerrados")
    public String redirigePaginaTicketsAtendidos(Model model) {
        atencionController.getListaTodosLosTicketsAtendidosAVista(model);
        clasificadoresController.getListaClasificacionesAtencionActivos(model);
        clasificadoresController.getListaTipoIncidenciaActivos(model);
        clasificadoresController.getListaClasificacionesUrgenciaActivos(model);
        model.addAttribute("Titulo", "HelpDesk | Soporte - Tickets Cerrados");
        return "soporte/ticketsAtendidos";
    }

    @GetMapping("/soporte/Tickets-Desestimados")
    public String redirigePaginaTicketsDesestimados(Model model) {
        atencionController.getListaTodosLosTicketsDesestimadosAVista(model);
        clasificadoresController.getListaTipoIncidenciaActivos(model);
        clasificadoresController.getListaClasificacionesDesestimacionActivos(model);
        model.addAttribute("Titulo", "HelpDesk | Soporte - Tickets Desestimados");
        return "soporte/ticketsDesestimados";
    }

    //NO SE UTILIZA MODEL PORQUE LOS DATOS SE OBTIENEN A TRAVÉS DE FETCH CON JS PARA ACTUALIZARLOS AUTOMATICAMENTE CADA X segundos
    @GetMapping("/soporte/Dashboard")
    public String redirigePaginaDashboardSoporte(Model model) {
        model.addAttribute("Titulo", "HelpDesk | Soporte - Dashboard");
        return "soporte/dashboard";
    }

    @GetMapping("/ticket/{codigo}")
    public String verTicketPorCodigo(@PathVariable("codigo") String codigo, Model model) {
        Optional<Ticket> ticketOptional = ticketRepository.findByCodigoTicket(codigo);
        clasificadoresController.getListaClasificacionesAtencionActivos(model);
        clasificadoresController.getListaClasificacionesEsperaActivos(model);
        clasificadoresController.getListaClasificacionesDesestimacionActivos(model);
        clasificadoresController.getListaClasificacionesUrgenciaActivos(model);
        clasificadoresController.getListaSedesActivos(model);
        clasificadoresController.getListaCatIncidenciaActivos(model);
        boolean avisoFueraDeHorario = false;



        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();
            Usuario usuarioLogeado = usuarioService.getUsuarioPorId(usuarioService.getIDdeUsuarioLogeado());
            RolUsuario rolUsuarioLogeado = usuarioLogeado.getRolUsuario();



            if (rolUsuarioLogeado.getId() == 1 && !Objects.equals(ticket.getUsuario().getId(), usuarioLogeado.getId())){
                return "redirect:/inicio?error=user-without-permissions"; // Redirige a la vista de inicio

            }



            if (Objects.equals(ticket.getUsuario().getId(), usuarioService.getIDdeUsuarioLogeado())) {
                List<DetalleEnEspera> listaDetalleEsperas = ticket.getListaDetalleEsperas();

                if (!listaDetalleEsperas.isEmpty()) {
                    DetalleEnEspera primerDetalleEspera = listaDetalleEsperas.get(0);
                    HorarioAtencionSoporte horarioAtencionSoporte = horarioAtencionSoporteRepository.findTopByOrderByIdDesc();

                    if (primerDetalleEspera.getClasificacionEspera().getId() == 1 && horarioAtencionSoporte.getHoraEntrada().equals(primerDetalleEspera.getHoraFin())) {
                        avisoFueraDeHorario = true;
                    }
                }
            }









            model.addAttribute("fueraDeHorario", avisoFueraDeHorario);
            DetalleTicketDTO detalleTicketDTO = new DetalleTicketDTO(ticket);
            model.addAttribute("detalle", detalleTicketDTO);
            model.addAttribute("Titulo", "HelpDesk | " + detalleTicketDTO.getTicket().getCodigoTicket());
        }


        return "general/ticket";
    }


    @GetMapping("/supervisor/dashboard-soporte")
    public String redirigePaginaDashboardSoporteSupervisor(Model model) {
        model.addAttribute("Titulo", "HelpDesk | Soporte - Dashboard");
        return "soporte/dashboard";
    }
    @GetMapping("/supervisor/dashboard-admin")
    public String redirigePaginaDashboardAdminSupervisor(Model model) {
        clasificadoresController.getListaClasificacionesUrgenciaActivos(model);
        clasificadoresController.getListaSedesActivos(model);
        clasificadoresController.getListaCatIncidenciaActivos(model);
        model.addAttribute("Titulo", "HelpDesk | Admin - Dashboard");
        return "admin/dashboard";
    }


}
