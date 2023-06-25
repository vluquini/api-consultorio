package com.api.consultorio.services;
/*
    Classe responsável por Agendar e Cancelar consultas.
*/
import com.api.consultorio.dtos.MedicoDTO;
import com.api.consultorio.entities.consulta.MotivoCancelamento;
import com.api.consultorio.services.validacoes.AgendamentoConsulta;
import com.api.consultorio.dtos.ConsultaDTO;
import com.api.consultorio.entities.consulta.Consulta;
import com.api.consultorio.entities.medico.Medico;
import com.api.consultorio.entities.paciente.Paciente;
import com.api.consultorio.repositories.ConsultaRepository;
import com.api.consultorio.repositories.MedicoRepository;
import com.api.consultorio.repositories.PacienteRepository;
import com.api.consultorio.services.validacoes.CancelamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ConsultaService {
    @Autowired
    ConsultaRepository consultaRepository;
    @Autowired
    MedicoRepository medicoRepository;
    @Autowired
    PacienteRepository pacienteRepository;
    AgendamentoConsulta agendamentoConsulta = new AgendamentoConsulta();
    CancelamentoConsulta cancelamentoConsulta = new CancelamentoConsulta();

    public ResponseEntity<ConsultaDTO> agendarConsulta(ConsultaDTO consultaDTO,
                                                       UriComponentsBuilder uriBuilder) throws Exception {
        validarExistenciaPaciente(consultaDTO);

        Medico medico = escolherOuVerificarDisponibilidadeMedico(consultaDTO);
        // Inicia um optional com base no id recebido na requisição
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(consultaDTO.paciente().getId());
        // Cria um objeto de Paciente para receber todos os dados...
        Paciente paciente = pacienteOptional.get();

        validarConsultasMarcadas(consultaDTO, medico);
        validarAtividadePacienteEMedico(paciente, medico);
        validarDiaHoraConsulta(consultaDTO.dataHora());

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

    public void validarExistenciaPaciente(ConsultaDTO consultaDTO) throws Exception {
        if (consultaDTO.paciente() == null) {
            throw new Exception("Não pode marcar uma consulta sem um paciente!");
        }
    }

    public Medico escolherOuVerificarDisponibilidadeMedico(ConsultaDTO consultaDTO) throws Exception {
        AgendamentoConsulta agendamentoConsulta = new AgendamentoConsulta();
        Optional<Medico> medicoOptional;
        if (consultaDTO.medico() == null) {
            medicoOptional = agendamentoConsulta.escolherMedicoAleatorio(medicoRepository);
        } else {
            medicoOptional = medicoRepository.findById(consultaDTO.medico().getId());
            if (agendamentoConsulta.verificarMedicoConsultaMarcada(medicoOptional.get(), consultaDTO, consultaRepository)) {
                throw new Exception("Médico já possui consulta nesse horário!");
            }
        }
        if (!medicoOptional.isPresent()) {
            throw new Exception("Médico não encontrado na base de dados.");
        }
        return medicoOptional.get();
    }

    public void validarConsultasMarcadas(ConsultaDTO consultaDTO, Medico medico) throws Exception {
        if(!consultaRepository.findAll().isEmpty()){
            if (agendamentoConsulta.validarDiaConsultaPaciente(consultaDTO, consultaRepository)) {
                throw new Exception("Não pode marcar consulta no mesmo dia para o mesmo paciente.");
            }
            if (agendamentoConsulta.verificarMedicoConsultaMarcada(medico, consultaDTO, consultaRepository)) {
                throw new Exception("Médico já possui consulta nessa horario!");
            }
        }
    }

    public void validarAtividadePacienteEMedico(Paciente paciente, Medico medico) throws Exception {
        if (!paciente.getAtivo()) {
            throw new Exception("Não é possível marcar consultas com pacientes inativos no sistema!");
        }
        if (!medico.getAtivo()) {
            throw new Exception("Não é possível marcar consultas com médicos inativos no sistema!");
        }
    }

    public void validarDiaHoraConsulta(LocalDateTime dataHora) throws Exception {
        if (!agendamentoConsulta.isDiaUtil(dataHora)) {
            throw new Exception("Dia inválido! A clínica não funciona aos domingos.");
        }
        if (!agendamentoConsulta.isHorarioPermitido(dataHora)) {
            throw new Exception(("Só é possível marcar consultas entre 7h e 19h!"));
        }
        if (!agendamentoConsulta.validarHorarioDeAntecedencia(dataHora)) {
            throw new Exception("A consulta deve ser marcada com pelo menos 30 minutos de antecedência!");
        }
    }

//    public void cancelarConsulta(Long id, MotivoCancelamento motivo) throws Exception{
//
//        Optional<Consulta> consultaOptional = consultaRepository.findById(id);
//        if(consultaOptional.isPresent()){
//            Consulta consultaCancelada = consultaOptional.get();
//            consultaCancelada.cancelarConsulta(motivo);
//            consultaRepository.save(consultaCancelada);
//        }else
//            throw new Exception("Consulta não encontrada.");
//    }

    public void cancelarConsulta(Long id, MotivoCancelamento motivo) throws Exception{
        if(!cancelamentoConsulta.verificarMotivo(motivo))
            throw new Exception("É necessário informar o motivo do cancelamento!");

        if(id == null)
            throw new Exception("É necessário informar o id da consulta a ser cancelada!");

        // Inicia um objeto com o horário atual do sistema
        LocalDateTime horaAtual = LocalDateTime.now();

        Optional<Consulta> consultaOptional = consultaRepository.findById(id);
        if (consultaOptional.isPresent()) {
            Consulta consultaCancelada = consultaOptional.get();

            LocalDateTime horarioConsulta = consultaCancelada.getDataHora();

            // Calcula a diferença entre as duas datas
            Duration diferenca = Duration.between(horaAtual, horarioConsulta);

            // Verifica se a diferença é menor que 24 horas
            if (diferenca.toHours() < 24) {
                throw new Exception("A consulta deve ser cancelada com antecedência mínima de 24 horas.");
            }

            consultaCancelada.cancelarConsulta(motivo);
            consultaRepository.save(consultaCancelada);
        } else {
            throw new Exception("Consulta não encontrada.");
        }
    }

//    public List<ConsultaDTO> listarConsultas() throws Exception {
//        if(!consultaRepository.findAll().isEmpty())
//            return consultaRepository.findAll().stream()
//                    .map(ConsultaDTO::new).toList();
//        throw new Exception("Não há consultas marcadas!");
//    }

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

}
