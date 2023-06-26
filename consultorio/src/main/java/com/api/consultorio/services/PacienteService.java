package com.api.consultorio.services;

import com.api.consultorio.dtos.MedicoListDTO;
import com.api.consultorio.dtos.PacienteDTO;
import com.api.consultorio.dtos.PacienteListDTO;
import com.api.consultorio.entities.medico.Medico;
import com.api.consultorio.entities.paciente.Paciente;
import com.api.consultorio.repositories.PacienteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
public class PacienteService {
    @Autowired
    PacienteRepository pacienteRepository;

    public ResponseEntity<PacienteDTO> cadastrar(PacienteDTO pacienteDTO, UriComponentsBuilder uriBuilder){
        Paciente paciente = new Paciente(pacienteDTO);
        URI uri = uriBuilder.path("/medicos/{id}").buildAndExpand(paciente.getId()).toUri();
        pacienteRepository.save(paciente);
        return ResponseEntity.created(uri).body(new PacienteDTO(paciente));
    }

//    public List<PacienteListDTO> listar() {
//        // retorna todos os elementos ordenados pelo nome
//        return pacienteRepository.findAll().stream()
//                // filtra apenas os médicos "ativos"
//                .filter(Paciente::getAtivo)
//                .sorted((m1, m2) -> m1.getNome()
//                .compareToIgnoreCase(m2.getNome())).map(PacienteListDTO::new).toList();
//    }

    public Page<PacienteListDTO> listar(int numeroPagina){
        int registrosPorPagina = 10;
        // Define a página desejada (baseada no número de página recebido)
        Pageable pageable = PageRequest.of(numeroPagina, registrosPorPagina);
        // Realiza a consulta paginada
        Page<Paciente> paginaPacientes = pacienteRepository.findAll(pageable);

        List<PacienteListDTO> pacienteListDTOS = paginaPacientes.stream()
                .filter(paciente -> paciente.getAtivo())
                .sorted(Comparator.comparing(Paciente::getNome, String.CASE_INSENSITIVE_ORDER))
                .map(PacienteListDTO::new)
                .toList();

        // Retorna a página de médicos
        return new PageImpl<>(pacienteListDTOS, pageable, paginaPacientes.getTotalElements());

    }

    public ResponseEntity<PacienteDTO> atualizar(Long id, PacienteDTO medicoDTO){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(id);
        // se existir um medico com o id passado, realiza a atualização
        if(optionalPaciente.isPresent()){
            Paciente existingPaciente = optionalPaciente.get();
            // o "terceiro" parâmetro são os campos a serem ignorados na atualização
            BeanUtils.copyProperties(medicoDTO, existingPaciente,
                    "id", "email", "cpf");

            Paciente pacienteUpdated = pacienteRepository.save(existingPaciente);

            return new ResponseEntity<>(new PacienteDTO(
                    pacienteUpdated.getId(),
                    pacienteUpdated.getNome(),
                    pacienteUpdated.getEmail(),
                    pacienteUpdated.getTelefone(),
                    pacienteUpdated.getCpf(),
                    pacienteUpdated.getEndereco(),
                    pacienteUpdated.getAtivo()),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<PacienteDTO> apagar(Long id){
        Optional<Paciente> optionalMedico = pacienteRepository.findById(id);

        if(optionalMedico.isPresent()){
            Paciente paciente = optionalMedico.get();
            // seta status do medico para "inativo"
            paciente.setAtivo(false);
            ResponseEntity<PacienteDTO> response = new ResponseEntity<>(new PacienteDTO(
                    paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getTelefone(),
                    paciente.getCpf(), paciente.getEndereco(), paciente.getAtivo()), HttpStatus.OK);
            // salva a nova instância atualizada
            pacienteRepository.save(paciente);
            return response;
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
