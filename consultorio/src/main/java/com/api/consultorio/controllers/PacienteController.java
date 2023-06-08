package com.api.consultorio.controllers;

import com.api.consultorio.dtos.PacienteDTO;
import com.api.consultorio.dtos.PacienteListDTO;
import com.api.consultorio.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    @Autowired
    private PacienteService pacienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PacienteDTO> cadastrar(@RequestBody PacienteDTO pacienteDTO, UriComponentsBuilder uriBuilder){
        return pacienteService.cadastrar(pacienteDTO, uriBuilder);
    }
    @GetMapping
    public List<PacienteListDTO> listar(){
        return pacienteService.listar();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> atualizar(@PathVariable Long id, @RequestBody PacienteDTO pacienteDTO){
        return pacienteService.atualizar(id, pacienteDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PacienteDTO> apagar(@PathVariable Long id){
        return pacienteService.apagar(id);
    }
}
