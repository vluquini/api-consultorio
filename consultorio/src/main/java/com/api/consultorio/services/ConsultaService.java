package com.api.consultorio.services;
/*
    Classe responsável por Agendar e Cancelar consultas.
*/
import com.api.consultorio.entities.consulta.MotivoCancelamento;
import com.api.consultorio.services.validacoes.ValidarDadosConsulta;
import com.api.consultorio.dtos.ConsultaDTO;
import com.api.consultorio.entities.consulta.Consulta;
import com.api.consultorio.entities.medico.Medico;
import com.api.consultorio.entities.paciente.Paciente;
import com.api.consultorio.repositories.ConsultaRepository;
import com.api.consultorio.repositories.MedicoRepository;
import com.api.consultorio.repositories.PacienteRepository;
import com.api.consultorio.services.validacoes.ValidacoesCancelamento;
import com.api.consultorio.services.validacoes.ValidarConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
Esta classe contém os métodos para agendar,
cancelar, e listar consultas.
 */
@Service
public class ConsultaService {
    @Autowired
    ConsultaRepository consultaRepository;
    @Autowired
    MedicoRepository medicoRepository;
    @Autowired
    PacienteRepository pacienteRepository;
    //ValidarDadosConsulta validarDadosConsulta = new ValidarDadosConsulta();
    ValidacoesCancelamento validacoesCancelamento = new ValidacoesCancelamento();
    ValidarConsulta validarConsulta = new ValidarConsulta();

    public ResponseEntity<ConsultaDTO> agendarConsulta(ConsultaDTO consultaDTO,
                                                       UriComponentsBuilder uriBuilder) throws Exception {
        validarConsulta.validarExistenciaPaciente(consultaDTO);

        Medico medico = validarConsulta.escolherOuVerificarDisponibilidadeMedico(consultaDTO, medicoRepository, consultaRepository);
        // Inicia um optional com base no id recebido na requisição
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(consultaDTO.paciente().getId());
        // Cria um objeto de Paciente para receber todos os dados...
        Paciente paciente = pacienteOptional.get();

        validarConsulta.validarConsultasMarcadas(consultaDTO, medico, consultaRepository);
        validarConsulta.validarAtividadePacienteEMedico(paciente, medico);
        validarConsulta.validarDiaHoraConsulta(consultaDTO.dataHora());

        Consulta consulta = new Consulta();

        consulta.setId(consultaDTO.id());
        consulta.setMedico(medico);
        consulta.setPaciente(paciente);
        consulta.setDataHora(consultaDTO.dataHora());
        consulta.setCancelada(false);

        URI uri = uriBuilder.path("/consultas/{id}").buildAndExpand(consulta.getId()).toUri();
        consultaRepository.save(consulta);
        return ResponseEntity.created(uri).body(new ConsultaDTO(consulta));
    }

    public void cancelarConsulta(Long id, MotivoCancelamento motivo) throws Exception{
        validacoesCancelamento.validarMotivoCancelamento(motivo);
        validacoesCancelamento.validarIdConsulta(id);

        // Inicia um objeto com o horário atual do sistema
        LocalDateTime horaAtual = LocalDateTime.now();
        Optional<Consulta> consultaOptional = consultaRepository.findById(id);

        if (consultaOptional.isPresent()) {
            Consulta consultaCancelada = consultaOptional.get();

            LocalDateTime horarioConsulta = consultaCancelada.getDataHora();

            // Calcula a diferença entre as duas datas
            Duration diferenca = Duration.between(horaAtual, horarioConsulta);

            // Verifica se a diferença é menor que 24 horas
            validacoesCancelamento.validarAntecedenciaCancelamento(diferenca);

            consultaCancelada.cancelarConsulta(motivo);
            consultaRepository.save(consultaCancelada);
        } else {
            throw new Exception("Consulta não encontrada.");
        }
    }

    public List<ConsultaDTO> listarConsultas() throws Exception {
        // Lista apenas as consultas com status cancelado "falso"
        List<ConsultaDTO> consultas = consultaRepository.findAll().stream()
                .filter(consulta -> !consulta.getCancelada()).map(ConsultaDTO::new)
                .collect(Collectors.toList());
        if (!consultas.isEmpty()) {
            return consultas;
        }
        throw new Exception("Não há consultas marcadas!");
    }

    public List<Consulta> listarConsultasCanceladas() throws Exception {
        if(!consultaRepository.findAll().isEmpty())
            return consultaRepository.findAll().stream()
                    .filter(consulta -> consulta.getCancelada())
                    .toList();
        throw new Exception("Não há consultas marcadas!");
    }
//    public List<ConsultaDTO> listarConsultas() throws Exception {
//        if(!consultaRepository.findAll().isEmpty())
//            return consultaRepository.findAll().stream()
//                    .map(ConsultaDTO::new).toList();
//        throw new Exception("Não há consultas marcadas!");
//    }

}
