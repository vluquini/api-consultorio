package com.api.consultorio.dtos;

import com.api.consultorio.entities.medico.Especialidade;
import com.api.consultorio.entities.medico.Medico;

public record MedicoListDTO(
        String nome,
        String email,
        String crm,
        Especialidade especialidade,
        Boolean ativo
) {
    public MedicoListDTO(Medico medico){
        this(medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getEspecialidade(),
                medico.getAtivo());
    }
}
