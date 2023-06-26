package com.api.consultorio.dtos;

import com.api.consultorio.entities.Endereco;
import com.api.consultorio.entities.paciente.Paciente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PacienteDTO(
        Long id,
        @NotBlank String nome,
        @NotBlank String email,
        @NotBlank String telefone,
        @NotBlank String cpf,
        @NotNull Endereco endereco,
        Boolean ativo
) {
    public PacienteDTO(Paciente paciente){
        this(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getTelefone(),
                paciente.getCpf(), paciente.getEndereco(), paciente.getAtivo());
    }
}
