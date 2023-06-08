package com.api.consultorio.services;

import com.api.consultorio.dtos.MedicoDTO;
import com.api.consultorio.dtos.MedicoListDTO;
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

    // para cadastrar um novo medico, todos os dados devem ser preenchidos na requisição.
    public ResponseEntity<MedicoDTO> cadastrar(MedicoDTO medicoDTO, UriComponentsBuilder uriBuilder){
        // Verifica se há algum campo nulo no objeto medicoDTO passado no parâmetro

        Medico medico = new Medico(medicoDTO);
        URI uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        medicoRepository.save(medico);
        return ResponseEntity.created(uri).body(new MedicoDTO(medico));
    }

    // este método retorna apenas os dados necessários dos médicos, usando a classe "MedicoResponseDTO".
    public List<MedicoListDTO> listar() {
        /*
        if (nome != null && !(nome.equalsIgnoreCase(""))) {
            // retorna os elementos que possuirem o mesmo nome
            return medicoRepository.findAll().stream()
                    .filter(medico -> medico.getNome().equalsIgnoreCase(nome) && medico.isAtivo() == true)
                    .map(MedicoListDTO::new)
                    .toList();
        }
        */
        // retorna todos os elementos ordenados pelo nome
        return medicoRepository.findAll().stream()
                // filtra apenas os médicos "ativos"
                .filter(medico -> medico.isAtivo() == true)
                .sorted((m1, m2) -> m1.getNome()
                .compareToIgnoreCase(m2.getNome())).map(MedicoListDTO::new).toList();
    }

    /*
    Este método atualiza apenas os campos "nome" e "telefone", ignorando
    os demais parâmetros que possam ser passados na requisição.
    Isto evita a necessidade de criar novas classes DTOs.
     */

    public ResponseEntity<MedicoDTO> atualizar(Long id, MedicoDTO medicoDTO){
        Optional<Medico> optionalMedico = medicoRepository.findById(id);
        // se existir um medico com o id passado, realiza a atualização
        if(optionalMedico.isPresent()){
            Medico existingMedico = optionalMedico.get();
            // o "terceiro" parâmetro são os campos a serem ignorados na atualização
            BeanUtils.copyProperties(medicoDTO, existingMedico,
                    "id", "email", "crm", "especialidade");

            Medico medicoUpdated = medicoRepository.save(existingMedico);

            return new ResponseEntity<MedicoDTO>(new MedicoDTO(
                    medicoUpdated.getId(),
                    medicoUpdated.getNome(),
                    medicoUpdated.getEmail(),
                    medicoUpdated.getTelefone(),
                    medicoUpdated.getCrm(),
                    medicoUpdated.getEspecialidade(),
                    medicoUpdated.isAtivo()),
                    HttpStatus.OK);
         }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // método responsável por alterar o atibuto "ativo" dos métodos. não apaga do banco.
    public ResponseEntity<MedicoDTO> apagar(Long id){
        Optional<Medico> optionalMedico = medicoRepository.findById(id);

        if(optionalMedico.isPresent()){
            Medico medico = optionalMedico.get();
            // seta status do medico para "inativo"
            medico.setAtivo(false);
            ResponseEntity<MedicoDTO> response = new ResponseEntity<MedicoDTO>(new MedicoDTO(
                    medico.getId(), medico.getNome(), medico.getEmail(), medico.getTelefone(), medico.getCrm(),
                    medico.getEspecialidade(), medico.isAtivo()), HttpStatus.OK);
            // salvo a nova instância atualizada
            medicoRepository.save(medico);
            return response;
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
