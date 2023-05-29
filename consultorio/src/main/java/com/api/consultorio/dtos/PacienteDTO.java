package com.api.consultorio.dtos;

import com.api.consultorio.entities.Paciente;
import jakarta.validation.constraints.NotNull;

public record PacienteDTO(
        Long id,
        @NotNull String nome,
        @NotNull String email,
        @NotNull String telefone,
        @NotNull String cpf
) {
    public PacienteDTO(Paciente paciente){
        this(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getTelefone(),
                paciente.getCpf());
    }
}
