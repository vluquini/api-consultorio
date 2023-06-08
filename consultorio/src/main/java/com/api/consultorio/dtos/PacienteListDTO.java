package com.api.consultorio.dtos;

import com.api.consultorio.entities.Paciente;

public record PacienteListDTO(
        String nome,
        String email,
        String cpf,
        boolean ativo
) {
    public PacienteListDTO(Paciente paciente){
        this(paciente.getNome(), paciente.getEmail(), paciente.getCpf(),paciente.isAtivo());
    }
}
