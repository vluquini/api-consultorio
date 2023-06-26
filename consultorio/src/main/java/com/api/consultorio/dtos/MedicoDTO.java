package com.api.consultorio.dtos;

import com.api.consultorio.entities.Endereco;
import com.api.consultorio.entities.medico.Especialidade;
import com.api.consultorio.entities.medico.Medico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MedicoDTO(
        Long id,
        @NotBlank String nome,
        @NotBlank String email,
        @NotBlank String telefone,
        @NotBlank String crm,
        @NotNull Especialidade especialidade,
        @NotNull Endereco endereco,
        Boolean ativo
) {
    public MedicoDTO(Medico medico){
        this(medico.getId(), medico.getNome(), medico.getEmail(),
                medico.getTelefone(), medico.getCrm(), medico.getEspecialidade(), medico.getEndereco(),
                medico.getAtivo());
    }
}
