package com.api.consultorio.services.validacoes;

import com.api.consultorio.dtos.ConsultaDTO;
import com.api.consultorio.entities.medico.Medico;
import com.api.consultorio.entities.paciente.Paciente;
import com.api.consultorio.repositories.ConsultaRepository;
import com.api.consultorio.repositories.MedicoRepository;

import java.time.LocalDateTime;
import java.util.Optional;
/*
Esta classe chama os métodos da classe "ValidarDadosConsulta"
para validar o agendamento de uma consulta.
 */
public class ValidarConsulta {
    ValidarDadosConsulta validarDadosConsulta = new ValidarDadosConsulta();
    public void validarExistenciaPaciente(ConsultaDTO consultaDTO) throws Exception {
        if (consultaDTO.paciente() == null) {
            throw new Exception("Não pode marcar uma consulta sem um paciente!");
        }
    }

    public Medico escolherOuVerificarDisponibilidadeMedico(ConsultaDTO consultaDTO, MedicoRepository medicoRepository,
                                                           ConsultaRepository consultaRepository) throws Exception {
        if(medicoRepository.findAll().isEmpty())
            throw new Exception("Não há médicos na base de dados!");

        ValidarDadosConsulta validarDadosConsulta = new ValidarDadosConsulta();
        Optional<Medico> medicoOptional;
        if (consultaDTO.medico() == null) {
            medicoOptional = validarDadosConsulta.escolherMedicoAleatorio(medicoRepository);
        } else {
            medicoOptional = medicoRepository.findById(consultaDTO.medico().getId());
            if (validarDadosConsulta.verificarMedicoConsultaMarcada(medicoOptional.get(), consultaDTO, consultaRepository)) {
                throw new Exception("Médico já possui consulta nesse horário!");
            }
        }
        if (!medicoOptional.isPresent()) {
            throw new Exception("Médico não encontrado na base de dados.");
        }
        return medicoOptional.get();
    }

    public void validarConsultasMarcadas(ConsultaDTO consultaDTO, Medico medico, ConsultaRepository consultaRepository) throws Exception {
        if(!consultaRepository.findAll().isEmpty()){
            if (validarDadosConsulta.validarDiaConsultaPaciente(consultaDTO, consultaRepository)) {
                throw new Exception("Não pode marcar consulta no mesmo dia para o mesmo paciente.");
            }
            if (validarDadosConsulta.verificarMedicoConsultaMarcada(medico, consultaDTO, consultaRepository)) {
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
        if (!validarDadosConsulta.isDiaUtil(dataHora)) {
            throw new Exception("Dia inválido! A clínica não funciona aos domingos.");
        }
        if (!validarDadosConsulta.isHorarioPermitido(dataHora)) {
            throw new Exception(("Só é possível marcar consultas entre 7h e 19h!"));
        }
        if (!validarDadosConsulta.validarHorarioDeAntecedencia(dataHora)) {
            throw new Exception("A consulta deve ser marcada com pelo menos 30 minutos de antecedência!");
        }
    }



}
