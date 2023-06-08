package com.api.consultorio.dtos;

import com.api.consultorio.entities.Especialidade;
import com.api.consultorio.entities.Medico;

public record MedicoListDTO(
        String nome,
        String email,
        String crm,
        Especialidade especialidade,
        boolean ativo
) {
    public MedicoListDTO(Medico medico){
        this(medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getEspecialidade(), medico.isAtivo());
    }
}
