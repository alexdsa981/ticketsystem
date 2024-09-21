package com.ipor.ticketsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {
    @GetMapping("/")
    public String redirectToInicio() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String redirigePaginaInicio() {
        // Redirige a index.html que está en static
        return "index";
    }

    @GetMapping("/hola")
    public String RedirigePaginaHola(){
        return "hola";
    }
    @GetMapping("/prueba")
    public String RedirigePaginaPrueba(@RequestParam(name="nombre", required = false, defaultValue = "default")
                                       String nombre, Model model){
        //clave valor "nombre" se utilizará en la pagina de destino prueba y colocará el String nombre
        model.addAttribute("nombre", nombre);
        return "prueba";
    }
}
