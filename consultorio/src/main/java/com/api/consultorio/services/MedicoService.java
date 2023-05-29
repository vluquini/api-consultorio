package com.api.consultorio.services;

import com.api.consultorio.dtos.MedicoDTO;
import com.api.consultorio.entities.Medico;
import com.api.consultorio.repositories.MedicoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class MedicoService {
    @Autowired
    MedicoRepository medicoRepository;

    public ResponseEntity<MedicoDTO> cadastrar(MedicoDTO medicoDTO, UriComponentsBuilder uriBuilder){
        Medico medico = new Medico(medicoDTO);
        URI uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        medicoRepository.save(medico);
        return ResponseEntity.created(uri).body(new MedicoDTO(medico));
    }


    public List<MedicoDTO> listar(String nome) {
        if(nome != null && !(nome.equalsIgnoreCase(""))){
            return medicoRepository.findAll().stream()
                    .filter(medico -> medico.getNome().equalsIgnoreCase(nome))
                    .map(MedicoDTO::new)
                    .toList();
        }
        return medicoRepository.findAll().stream().map(MedicoDTO::new).toList();
    }

    public ResponseEntity<MedicoDTO> atualizar(Long id, MedicoDTO medicoDTO){
        Optional<Medico> optionalMedico = medicoRepository.findById(id);
        // se existir um medico com o id passado, realiza a atualização
        if(optionalMedico.isPresent()){
            Medico existingMedico = optionalMedico.get();
            BeanUtils.copyProperties(medicoDTO, existingMedico);

            Medico medicoUpdated = medicoRepository.save(existingMedico);

            return new ResponseEntity<MedicoDTO>(new MedicoDTO(
                    medicoUpdated.getId(),
                    medicoUpdated.getNome(),
                    medicoUpdated.getEmail(),
                    medicoUpdated.getTelefone(),
                    medicoUpdated.getCrm(),
                    medicoUpdated.getEspecialidade()),
                    HttpStatus.OK);
         }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
