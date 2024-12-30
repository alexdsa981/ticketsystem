package com.ipor.ticketsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {
    @Autowired
    private TicketController ticketController;
    @Autowired
    private AtencionController atencionController;
    @Autowired
    private UsuariosCRUDController usuariosCRUDController;
    @Autowired
    private ClasificadoresController clasificadoresController;
    @Autowired
    private LoginController loginController;

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
        clasificadoresController.getListaClasificacionIncidenciaActivos(model);
        model.addAttribute("Titulo", "HelpDesk | Inicio");
        return "inicio"; // Redirige a la vista 'inicio.html'
    }

    @GetMapping("/TicketsEnProceso")
    public String redirigePaginaTicketsEnProceso(Model model) {
        atencionController.getListaMisTicketsRecepcionadosAVista(model);
        model.addAttribute("Titulo", "HelpDesk | En Proceso");
        return "enProceso";
    }

    @GetMapping("/TicketsAtendidos")
    public String redirigePaginaMisTicketsAtendidos(Model model) {
        atencionController.getListaMisTicketsAtendidosAVista(model);
        model.addAttribute("Titulo", "HelpDesk | Atendidos");
        return "atendidos";
    }
    @GetMapping("/TicketsDesestimados")
    public String redirigePaginaMisTicketsDesestimados(Model model) {
        atencionController.getListaMisTicketsDesestimadosAVista(model);
        model.addAttribute("Titulo", "HelpDesk | Desestimados");
        return "desestimados";
    }

    //NO SE UTILIZA MODEL PORQUE LOS DATOS SE OBTIENEN A TRAVÉS DE FETCH CON JS PARA ACTUALIZARLOS AUTOMATICAMENTE CADA X segundos
    @GetMapping("/admin/Dashboard")
    public String redirigePaginaDashboardAdmin(Model model) {
        model.addAttribute("Titulo", "HelpDesk | Admin - Dashboard");
        return "admin/dashboard";
    }

    @GetMapping("/admin/Usuarios")
    public String redirigePaginaUsuarios(Model model) {
        usuariosCRUDController.listarUsuarios(model);
        usuariosCRUDController.listarRoles(model);
        model.addAttribute("Titulo", "HelpDesk | Admin - Gestion de Usuarios");

        return "admin/usuarios";
    }

    @GetMapping("/admin/Clasificadores")
    public String redirigePaginaClasiicadores(Model model) {
        clasificadoresController.listarClasificadores(model);
        model.addAttribute("Titulo", "HelpDesk | Admin - Gestion de Clasificadores");
        return "admin/clasificadores";
    }

    @GetMapping("/soporte/Recepcionar")
    public String redirigePaginaTicketsRecibidos(Model model) {
        ticketController.retornaTicketsRecibidosAVista(model);
        clasificadoresController.getListaClasificacionesUrgenciaActivos(model);
        clasificadoresController.getListaClasificacionesDesestimacionActivos(model);
        clasificadoresController.getListaClasificacionIncidenciaActivos(model);
        clasificadoresController.getListaTipoComponentesActivos(model);
        model.addAttribute("Titulo", "HelpDesk | Soporte - Recepcionar Tickets");
        return "/soporte/ticketsRecibidos";
    }

    @GetMapping("/soporte/Atender")
    public String redirigePaginaTicketsRecepcionados(Model model) {
        atencionController.getListaTodosLosTicketsRecepcionadosAVista(model);
        clasificadoresController.getListaClasificacionesServicioActivos(model);
        clasificadoresController.getListaClasificacionIncidenciaActivos(model);
        clasificadoresController.getListaClasificacionesDesestimacionActivos(model);
        clasificadoresController.getListaClasificacionesUrgenciaActivos(model);
        model.addAttribute("Titulo", "HelpDesk | Soporte - Atender Tickets");
        return "soporte/ticketsRecepcionados";
    }

    @GetMapping("/soporte/Tickets-Cerrados")
    public String redirigePaginaTicketsAtendidos(Model model) {
        atencionController.getListaTodosLosTicketsAtendidosAVista(model);
        clasificadoresController.getListaClasificacionesServicioActivos(model);
        clasificadoresController.getListaClasificacionIncidenciaActivos(model);
        clasificadoresController.getListaClasificacionesUrgenciaActivos(model);
        model.addAttribute("Titulo", "HelpDesk | Soporte - Tickets Cerrados");
        return "soporte/ticketsAtendidos";
    }
    @GetMapping("/soporte/Tickets-Desestimados")
    public String redirigePaginaTicketsDesestimados(Model model) {
        atencionController.getListaTodosLosTicketsDesestimadosAVista(model);
        clasificadoresController.getListaClasificacionIncidenciaActivos(model);
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





    @GetMapping("/direccion/Dashboard")
    public String redirigePaginaDashboardDireccion(Model model) {
        model.addAttribute("Titulo", "HelpDesk | Dirección - Dashboard");
        return "direccion/dashboard";
    }
    //Metodo para manejar fragment expression
    @GetMapping("/direccion/Recibidos")
    public String redirigePaginaTicketsRecibidosDireccion(Model model) {
        clasificadoresController.getListaClasificacionesUrgenciaActivos(model);
        clasificadoresController.getListaClasificacionesDesestimacionActivos(model);
        clasificadoresController.getListaTipoComponentesActivos(model);
        ticketController.retornaTicketsRecibidosAVistaDireccion(model);
        model.addAttribute("Titulo", "HelpDesk | Dirección - Tickets Recibidos");
        return "direccion/ticketsRedirigidos";
    }
    //Metodo para manejar fragment expression
    @GetMapping("/direccion/Historial")
    public String redirigePaginaHistorialDireccion(Model model) {
        atencionController.getListaTodosLosTicketsRecepcionadosPorDireccionAVista(model);
        model.addAttribute("Titulo", "HelpDesk | Dirección - Recepciones Realizadas");
        return "direccion/historialRecepcion";
    }
    //Metodo para manejar fragment expression
    @GetMapping("/fragment-expression")
    public String fragmentExpression() {

        return "fragment-expression";
    }

}
