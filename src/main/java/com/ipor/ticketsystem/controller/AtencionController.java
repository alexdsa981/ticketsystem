package com.ipor.ticketsystem.controller;

import com.ipor.ticketsystem.model.dto.AtencionTicketDTO;
import com.ipor.ticketsystem.model.dynamic.Recepcion;
import com.ipor.ticketsystem.service.AtencionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/app/tickets")
public class AtencionController {
    @Autowired
    AtencionService atencionService;
    //metodo para enviar todos los tickets en proceso a "enProceso"
    public Model retornaTicketsEnProcesoAVista(Model model) {
        List<AtencionTicketDTO> AllRecepcionados =  atencionService.getListaRecepcionados();
        model.addAttribute("AllRecepcionados", AllRecepcionados);
        return  model;
    }
    //metodo para enviar todos los tickets atendidos a "atendidos"
    public Model retornaTicketsAtendidosAVista(Model model) {
        List<AtencionTicketDTO> AllAtendidos = atencionService.getListaAtendidos();
        model.addAttribute("AllAtendidos", AllAtendidos);
        return  model;
    }
}
