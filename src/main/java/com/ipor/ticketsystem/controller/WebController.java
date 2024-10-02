package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {
    @Autowired
    TicketService ticketService;
    @Autowired
    TicketController ticketController;

    //redirige / a /login
    @GetMapping("/")
    public String redirectToInicio() {
        return "redirect:/login";
    }

    //abre index.html en /login
    @GetMapping("/login")
    public String redirigePaginaLogin() {
        // Redirige a index.html que está en static
        return "index";
    }

    // Método para manejar la vista de inicio y mostrar los tickets
    @GetMapping("/inicio")
    public String redirigePaginaInicio(Model model) {
        ticketController.retornaTicketsPropiosAInicio(model); // Llama al método que agrega tickets
        return "inicio"; // Redirige a la vista 'inicio.html'
    }

    //Metodo para manejar fragment expression
    @GetMapping("/fragment-expression")
    public String fragmentExpression(){
        return  "fragment-expression";
    }

}
