package com.integratronic.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.integratronic.dto.InformeVentasResponseDTO;
import com.integratronic.service.InformeService;

@RestController
@RequestMapping("/api/informes")
public class InformeController {

    private final InformeService informeService;

    public InformeController(InformeService informeService) {
        this.informeService = informeService;
    }

    @GetMapping("/ventas")
    public InformeVentasResponseDTO obtenerInformeVentas() {
        return informeService.obtenerInformeVentas();
    }
}
