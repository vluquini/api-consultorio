package com.api.consultorio.controllers;

import com.api.consultorio.dtos.ConsultaDTO;
import com.api.consultorio.entities.consulta.Consulta;
import com.api.consultorio.entities.consulta.MotivoCancelamento;
import com.api.consultorio.services.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {
    @Autowired
    private ConsultaService consultaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ConsultaDTO> agendarConsulta(@RequestBody ConsultaDTO consultaDTO,
                                                       UriComponentsBuilder uriBuilder) throws Exception {
         return consultaService.agendarConsulta(consultaDTO, uriBuilder);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarConsulta(@PathVariable Long id,
                                       @RequestBody MotivoCancelamento motivo) throws Exception {
        consultaService.cancelarConsulta(id, motivo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<ConsultaDTO> listarConsultas()throws Exception{
        return consultaService.listarConsultas();
    }

    @GetMapping("/consultas_canceladas")
    public List<Consulta> listarConsultasCanceladas() throws Exception{
        return consultaService.listarConsultasCanceladas();
    }

}
