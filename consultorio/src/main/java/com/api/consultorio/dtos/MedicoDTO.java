package com.api.consultorio.dtos;

import com.api.consultorio.entities.Especialidade;
import com.api.consultorio.entities.Medico;
import jakarta.validation.constraints.NotNull;

public record MedicoDTO(
        Long id,
         @NotNull String nome,
         @NotNull String email,
         @NotNull String telefone,
         @NotNull String crm,
         @NotNull Especialidade especialidade,
        @NotNull boolean ativo
) {
    public MedicoDTO(Medico medico){
        this(medico.getId(), medico.getNome(), medico.getEmail(),
                medico.getTelefone(), medico.getCrm(), medico.getEspecialidade(), medico.isAtivo());
    }
}
