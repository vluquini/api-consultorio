package com.api.consultorio.repositories;

import com.api.consultorio.dtos.ConsultaDTO;
import com.api.consultorio.entities.consulta.Consulta;
import com.api.consultorio.entities.medico.Medico;
import com.api.consultorio.entities.paciente.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
//        @Query("SELECT COUNT(c) > 0 FROM consulta c WHERE c.medico.id = :medicoId AND c.dataHora = :dataHora")
//        boolean existsByMedicoAndDataHora(@Param("medicoId") Long medicoId, @Param("dataHora") LocalDateTime dataHora);
//
//        public boolean existsByMedicoAndDataHora(Medico medico, LocalDateTime dataHora);
//
//        public boolean existsByPacienteAndDataHora(Paciente paciente, LocalDateTime dataHora);


}


