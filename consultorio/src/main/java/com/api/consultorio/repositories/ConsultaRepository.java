package com.api.consultorio.repositories;

import com.api.consultorio.entities.consulta.Consulta;
import com.api.consultorio.entities.medico.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    boolean existsByMedicoAndDataHora(Medico medico, LocalDateTime dataHora);

//  boolean existsByMedicoAndDataHoraBetween(Medico medico, LocalDateTime dataHora, LocalDateTime horaLimite);
}


