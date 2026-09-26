package com.tarea.financiera.controller;

import com.tarea.financiera.model.Credito;
import com.tarea.financiera.service.CreditoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CreditoController {

    private final CreditoService creditoService;

    public CreditoController(CreditoService creditoService) {
        this.creditoService = creditoService;
    }

    @GetMapping("/")
    public String mostrarFormulario(Model model) {
        model.addAttribute("credito", new Credito());
        return "formulario";
    }

    @PostMapping("/guardar")
    public String guardarCredito(
            @ModelAttribute Credito credito,
            Model model) {

        Credito creditoGuardado = creditoService.guardarCredito(credito);

        model.addAttribute("credito", creditoGuardado);

        return "resultado";
    }
}