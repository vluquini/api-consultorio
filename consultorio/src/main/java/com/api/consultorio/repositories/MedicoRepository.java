package com.api.consultorio.repositories;

import com.api.consultorio.entities.medico.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    // se o nome do método for "...byNameContaining" não funciona...
    //List<Medico> findByNomeContaining(String nome);
}
