package com.api.consultorio.repositories;

import com.api.consultorio.entities.consulta.Consulta;
import com.api.consultorio.entities.medico.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    //boolean existsByMedicoAndDataHora(Medico medico, LocalDateTime dataHora);
    boolean existsByMedicoAndDataHoraAndCanceladaFalse(Medico medico, LocalDateTime dataHora);

    @Query("SELECT c FROM consulta c WHERE c.cancelada = false")
    List<Consulta> findByCanceladaFalse();
}


