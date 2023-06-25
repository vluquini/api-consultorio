package com.api.consultorio.controllers;

import com.api.consultorio.dtos.MedicoDTO;
import com.api.consultorio.dtos.MedicoListDTO;
import com.api.consultorio.services.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {
    @Autowired
    private MedicoService medicoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MedicoDTO> cadastrar(@RequestBody MedicoDTO medicoDTO, UriComponentsBuilder uriBuilder){
        return medicoService.cadastrar(medicoDTO, uriBuilder);
    }

//    @GetMapping
//    public List<MedicoListDTO> listar(@RequestParam(required = false) String nome){
//        return medicoService.listar();
//    }

    @GetMapping
    public ResponseEntity<Page<MedicoListDTO>> listarMedicos(@RequestParam(defaultValue = "0") int numeroPagina) {
        Page<MedicoListDTO> paginaMedicos = medicoService.listar(numeroPagina);
        return ResponseEntity.ok(paginaMedicos);
    }
    @PutMapping("/{id}")
    public ResponseEntity<MedicoDTO> atualizar(@PathVariable Long id, @RequestBody MedicoDTO medicoDTO){
        return medicoService.atualizar(id, medicoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MedicoDTO> apagar(@PathVariable Long id){
        return medicoService.apagar(id);
    }
}
