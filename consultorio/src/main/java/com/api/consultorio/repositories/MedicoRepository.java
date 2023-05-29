package com.api.consultorio.repositories;

import com.api.consultorio.entities.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    // se o nome do método for "...byNameContaining" não funciona...
    //List<Medico> findByNomeContaining(String nome);
}
