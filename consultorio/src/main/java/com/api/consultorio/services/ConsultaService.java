package com.api.consultorio.services;
/*
    Classe responsável por Agendar e Cancelar consultas.
*/
import com.api.consultorio.services.validacoes.ValidacoesDataConsulta;
import com.api.consultorio.dtos.ConsultaDTO;
import com.api.consultorio.entities.consulta.Consulta;
import com.api.consultorio.entities.medico.Medico;
import com.api.consultorio.entities.paciente.Paciente;
import com.api.consultorio.repositories.ConsultaRepository;
import com.api.consultorio.repositories.MedicoRepository;
import com.api.consultorio.repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class ConsultaService {
    @Autowired
    ConsultaRepository consultaRepository;
    @Autowired
    MedicoRepository medicoRepository;
    @Autowired
    PacienteRepository pacienteRepository;
    ValidacoesDataConsulta validacoesDataConsulta = new ValidacoesDataConsulta();

    public void agendarConsulta(ConsultaDTO consultaDTO) throws Exception {
        validarExistenciaPaciente(consultaDTO);

        Medico medico = escolherOuVerificarDisponibilidadeMedico(consultaDTO);
        Optional<Paciente> paciente = pacienteRepository.findById(consultaDTO.paciente().getId());

        validarConsultasMarcadas(consultaDTO, medico);
        validarAtividadePacienteEMedico(paciente.get(), medico);
        validarDiaHoraConsulta(consultaDTO.dataHora());

        Consulta consulta = new Consulta();
        consulta.setId(consultaDTO.id());
        consulta.setMedico(medico);
        consulta.setPaciente(consultaDTO.paciente());
        consulta.setDataHora(consultaDTO.dataHora());

        consultaRepository.save(consulta);
    }

    private void validarExistenciaPaciente(ConsultaDTO consultaDTO) throws Exception {
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(consultaDTO.paciente().getId());
        if (!pacienteOptional.isPresent()) {
            throw new Exception("Paciente não encontrado na base de dados.");
        }
    }

    private Medico escolherOuVerificarDisponibilidadeMedico(ConsultaDTO consultaDTO) throws Exception {
        ValidacoesDataConsulta validacoesDataConsulta = new ValidacoesDataConsulta();
        Optional<Medico> medicoOptional;
        if (consultaDTO.medico() == null) {
            medicoOptional = validacoesDataConsulta.escolherMedicoAleatorio(medicoRepository);
        } else {
            medicoOptional = medicoRepository.findById(consultaDTO.medico().getId());
            if (validacoesDataConsulta.verificarMedicoConsultaMarcada(medicoOptional.get(), consultaDTO, consultaRepository)) {
                throw new Exception("Médico já possui consulta nesse horário!");
            }
        }
        if (!medicoOptional.isPresent()) {
            throw new Exception("Médico não encontrado na base de dados.");
        }
        return medicoOptional.get();
    }

    private void validarConsultasMarcadas(ConsultaDTO consultaDTO, Medico medico) throws Exception {
        if(!consultaRepository.findAll().isEmpty()){
            if (validacoesDataConsulta.validarDiaConsultaPaciente(consultaDTO, consultaRepository)) {
                throw new Exception("Não pode marcar consulta no mesmo dia para o mesmo paciente.");
            }
            if (validacoesDataConsulta.verificarMedicoConsultaMarcada(medico, consultaDTO, consultaRepository)) {
                throw new Exception("Médico já possui consulta nessa horario!");
            }
        }
    }

    private void validarAtividadePacienteEMedico(Paciente paciente, Medico medico) throws Exception {
        if (!paciente.getAtivo()) {
            throw new Exception("Não é possível marcar consultas com pacientes inativos no sistema!");
        }
        if (!medico.getAtivo()) {
            throw new Exception("Não é possível marcar consultas com médicos inativos no sistema!");
        }
    }

    private void validarDiaHoraConsulta(LocalDateTime dataHora) throws Exception {
        if (!ValidacoesDataConsulta.isDiaUtil(dataHora)) {
            throw new Exception("Dia inválido! A clínica não funciona aos domingos.");
        }
        if (!ValidacoesDataConsulta.isHorarioPermitido(dataHora)) {
            throw new Exception(("Só é possível marcar consultas entre 7h e 19h!"));
        }
        if (!ValidacoesDataConsulta.validarHorarioDeAntecedencia(dataHora)) {
            throw new Exception("A consulta deve ser marcada com pelo menos 30 minutos de antecedência!");
        }
    }

    public List<ConsultaDTO> listarConsultas() throws Exception {
        if(!consultaRepository.findAll().isEmpty())
            return consultaRepository.findAll().stream().map(ConsultaDTO::new).toList();
        throw new Exception("Não há consultas marcadas!");
    }

}
