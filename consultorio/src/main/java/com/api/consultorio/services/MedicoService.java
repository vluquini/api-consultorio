package com.api.consultorio.services;

import com.api.consultorio.dtos.MedicoDTO;
import com.api.consultorio.dtos.MedicoListDTO;
import com.api.consultorio.entities.medico.Medico;
import com.api.consultorio.repositories.MedicoRepository;
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
public class MedicoService {
    @Autowired
    MedicoRepository medicoRepository;

    // para cadastrar um novo medico, todos os dados devem ser preenchidos na requisição
    public ResponseEntity<MedicoDTO> cadastrar(MedicoDTO medicoDTO, UriComponentsBuilder uriBuilder) throws Exception {
        if(!enderecoVazio(medicoDTO)){
            throw new Exception("Somente 'numero' e 'complemento' do endereço podem ser vazios!");
        }

        Medico medico = new Medico(medicoDTO);
        URI uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        medicoRepository.save(medico);
        return ResponseEntity.created(uri).body(new MedicoDTO(medico));
    }

    public boolean enderecoVazio(MedicoDTO medicoDTO) {
        if (medicoDTO.endereco().getLogradouro().isBlank() ||
            medicoDTO.endereco().getNumero().isBlank() ||
            medicoDTO.endereco().getBairro().isBlank() ||
            medicoDTO.endereco().getCidade().isBlank() ||
            medicoDTO.endereco().getUf().isBlank() || medicoDTO.endereco().getCep().isBlank()) {
            return false;
        }
        return true;
    }


    // este método retorna apenas os dados necessários dos médicos, usando a classe "MedicoListDTO".
//    public List<MedicoListDTO> listar() {
//        // retorna todos os elementos ordenados pelo nome
//        return medicoRepository.findAll().stream()
//                // filtra apenas os médicos "ativos"
//                .filter(medico -> medico.getAtivo() == true)
//                .sorted((m1, m2) -> m1.getNome()
//                .compareToIgnoreCase(m2.getNome())).map(MedicoListDTO::new).toList();
//    }

    public Page<MedicoListDTO> listar(int numeroPagina) {
        int registrosPorPagina = 10;
        // Define a página desejada (baseada no número de página recebido)
        Pageable pageable = PageRequest.of(numeroPagina, registrosPorPagina);
        // Realiza a consulta paginada
        Page<Medico> paginaMedicos = medicoRepository.findAll(pageable);

        List<MedicoListDTO> medicoListDTOs = paginaMedicos.stream()
                .filter(medico -> medico.getAtivo())
                .sorted(Comparator.comparing(Medico::getNome, String.CASE_INSENSITIVE_ORDER))
                .map(MedicoListDTO::new)
                .toList();

        // Retorna a página de médicos
        return new PageImpl<>(medicoListDTOs, pageable, paginaMedicos.getTotalElements());
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
                    medicoUpdated.getEndereco(),
                    medicoUpdated.getAtivo()),
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
                    medico.getEspecialidade(), medico.getEndereco(), medico.getAtivo()), HttpStatus.OK);
            // salva a nova instância atualizada
            medicoRepository.save(medico);
            return response;
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
