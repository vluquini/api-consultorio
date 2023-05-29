package com.api.consultorio.dtos;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ConsultaDTO(
        Long id,
        @NotNull MedicoDTO medico,
        @NotNull PacienteDTO paciente,
        @NotNull LocalDate dataHora
) {
}
