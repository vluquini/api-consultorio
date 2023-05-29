package com.api.consultorio.repositories;

import com.api.consultorio.entities.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
}
