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
        ticketController.retornaTicketsPropiosYDatosInicialesAVista(model); // Llama al método que agrega tickets y datos iniciales relacionados a tickets
        atencionController.retornaListaClasificacionesUrgencia(model);
        return "inicio"; // Redirige a la vista 'inicio.html'
    }
    @GetMapping("/TicketsEnProceso")
    public String redirigePaginaTicketsEnProceso(Model model){
        atencionController.retornaTicketsEnProcesoAVista(model);
        atencionController.retornaListaClasificacionesServicio(model);

        return  "enProceso";
    }
    @GetMapping("/TicketsAtendidos")
    public String redirigePaginaTicketsAtendidos(Model model){
        atencionController.retornaTicketsAtendidosAVista(model);
        return  "atendidos";
    }

    @GetMapping("/Usuarios")
    public String redirigePaginaUsuarios(Model model){
        usuariosCRUDController.listarUsuarios(model);
        usuariosCRUDController.listarRoles(model);
        return  "usuarios";
    }







    //Metodo para manejar fragment expression
    @GetMapping("/fragment-expression")
    public String fragmentExpression(){
        return  "fragment-expression";
    }

}
