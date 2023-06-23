package com.api.consultorio.controllers;

import com.api.consultorio.dtos.ConsultaDTO;
import com.api.consultorio.services.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {
    @Autowired
    private ConsultaService consultaService;

    @PostMapping
    public void agendarConsulta(@RequestBody ConsultaDTO consultaDTO) throws Exception {
         consultaService.agendarConsulta(consultaDTO);

    }

    @GetMapping
    public List<ConsultaDTO> listarConsultas()throws Exception{
        return consultaService.listarConsultas();
    }

}
