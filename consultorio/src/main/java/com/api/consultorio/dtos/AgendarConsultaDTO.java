package com.api.consultorio.dtos;

import com.api.consultorio.entities.consulta.Consulta;
import com.api.consultorio.entities.medico.Especialidade;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record AgendarConsultaDTO(
        Long id,
        Long idMedico,
        @NotBlank
        Long idPaciente,
        @NotBlank
        @Future // valida data atual com uma posterior
        LocalDateTime dataHora
) {
        public AgendarConsultaDTO(Consulta consulta){
                this(consulta.getId(), consulta.getMedico().getId(), consulta.getPaciente().getId(),
                        consulta.getDataHora());
        }

}
