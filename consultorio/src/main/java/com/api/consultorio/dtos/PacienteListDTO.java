package com.api.consultorio.dtos;

import com.api.consultorio.entities.paciente.Paciente;

public record PacienteListDTO(
        String nome,
        String email,
        String cpf,
        Boolean ativo
) {
    public PacienteListDTO(Paciente paciente){
        this(paciente.getNome(), paciente.getEmail(), paciente.getCpf(),paciente.getAtivo());
    }
}
