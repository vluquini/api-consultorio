package com.api.consultorio.controllers;

import com.api.consultorio.dtos.ConsultaDTO;
import com.api.consultorio.entities.consulta.Consulta;
import com.api.consultorio.entities.consulta.MotivoCancelamento;
import com.api.consultorio.services.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
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
