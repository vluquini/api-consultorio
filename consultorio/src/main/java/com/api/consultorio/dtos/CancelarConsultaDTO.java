package com.api.consultorio.dtos;

import com.api.consultorio.entities.consulta.MotivoCancelamento;
import jakarta.validation.constraints.NotNull;

public record CancelarConsultaDTO(
        @NotNull
        Long idConsulta,

        @NotNull
        MotivoCancelamento motivo
) {

}
