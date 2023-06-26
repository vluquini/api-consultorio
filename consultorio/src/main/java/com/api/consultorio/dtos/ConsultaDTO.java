package com.api.consultorio.dtos;

import com.api.consultorio.entities.consulta.Consulta;
import com.api.consultorio.entities.consulta.MotivoCancelamento;
import com.api.consultorio.entities.medico.Especialidade;
import com.api.consultorio.entities.medico.Medico;
import com.api.consultorio.entities.paciente.Paciente;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ConsultaDTO(
        Long id,
        Medico medico,
        @NotNull
        Paciente paciente,
        @Future // valida data atual com uma posterior
        @NotBlank
        LocalDateTime dataHora,
        Boolean cancelada
       //Especialidade especialidade
) {
        public ConsultaDTO(Consulta consulta){
                this(consulta.getId(), consulta.getMedico(), consulta.getPaciente(),
                        consulta.getDataHora(), false);
        }

}
