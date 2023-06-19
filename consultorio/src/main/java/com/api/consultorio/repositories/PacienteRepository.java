package com.api.consultorio.repositories;

import com.api.consultorio.entities.paciente.Paciente;
import org.apache.el.stream.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
