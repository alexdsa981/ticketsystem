package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @Autowired
    TicketController ticketController;
    @Autowired
    AtencionController atencionController;
    @Autowired
    UsuariosCRUDController usuariosCRUDController;
    @Autowired
    ClasificadoresCRUDController clasificadoresCRUDController;

    //redirige / a /login
    @GetMapping("/")
    public String redirectToInicio() {
        return "redirect:/login";
    }

    //abre index.html en /login
    @GetMapping("/login")
    public String redirigePaginaLogin() {
        // Obtén la autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Si el usuario está autenticado y no es anónimo, redirige a la página de inicio
        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/inicio"; // Redirige a la vista de inicio
        }

        // Si no está autenticado, redirige a la vista de login (index.html en este caso)
        return "index";
    }

    // Método para manejar la vista de inicio y mostrar los tickets
    @GetMapping("/inicio")
    public String redirigePaginaInicio(Model model) {
        ticketController.retornaTicketsPropiosAVista(model);
        ticketController.retornaListaClasificacionIncidencia(model);
        model.addAttribute("Titulo", "HelpDesk - Inicio");
        return "inicio"; // Redirige a la vista 'inicio.html'
    }

    @GetMapping("/TicketsEnProceso")
    public String redirigePaginaTicketsEnProceso(Model model) {
        atencionController.retornaMisTicketsEnProcesoAVista(model);
        model.addAttribute("Titulo", "HelpDesk - En Proceso");
        return "enProceso";
    }

    @GetMapping("/TicketsAtendidos")
    public String redirigePaginaMisTicketsAtendidos(Model model) {
        atencionController.retornaMisTicketsAtendidosAVista(model);
        model.addAttribute("Titulo", "HelpDesk - Atendidos");
        return "atendidos";
    }

    @GetMapping("/admin/Usuarios")
    public String redirigePaginaUsuarios(Model model) {
        usuariosCRUDController.listarUsuarios(model);
        usuariosCRUDController.listarRoles(model);
        model.addAttribute("Titulo", "HelpDesk - Usuarios");

        return "admin/usuarios";
    }

    @GetMapping("/admin/Clasificadores")
    public String redirigePaginaClasiicadores(Model model) {
        clasificadoresCRUDController.listarClasificadores(model);
        model.addAttribute("Titulo", "HelpDesk - Clasificadores");
        return "admin/clasificadores";
    }

    @GetMapping("/soporte/Recibidos")
    public String redirigePaginaTicketsRecibidos(Model model) {
        ticketController.retornaTicketRecibidosAVista(model);
        atencionController.retornaListaClasificacionesUrgencia(model);
        ticketController.retornaListaClasificacionIncidencia(model);
        model.addAttribute("Titulo", "HelpDesk - Recibidos");
        return "/soporte/ticketsRecibidos";
    }

    @GetMapping("/soporte/Recepcionados")
    public String redirigePaginaTicketsRecepcionados(Model model) {
        atencionController.retornaTodosLosTicketsEnProcesoAVista(model);
        atencionController.retornaListaClasificacionesServicio(model);
        ticketController.retornaListaClasificacionIncidencia(model);
        atencionController.retornaListaClasificacionesUrgencia(model);
        model.addAttribute("Titulo", "HelpDesk - Recepcionados");
        return "soporte/ticketsRecepcionados";
    }

    @GetMapping("/soporte/Atendidos")
    public String redirigePaginaTicketsAtendidos(Model model) {
        atencionController.retornaTodosLosTicketsAtendidosAVista(model);
        model.addAttribute("Titulo", "HelpDesk - Cerrados");
        return "soporte/ticketsAtendidos";
    }


    //Metodo para manejar fragment expression
    @GetMapping("/fragment-expression")
    public String fragmentExpression() {

        return "fragment-expression";
    }

}
