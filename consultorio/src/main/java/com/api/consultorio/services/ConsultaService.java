package com.api.consultorio.services;
/*
    Classe responsável por Agendar e Cancelar consultas.
*/
import com.api.consultorio.dtos.AgendarConsultaDTO;
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

    public void agendarConsulta(ConsultaDTO consultaDTO) throws Exception{
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(consultaDTO.paciente().getId());
        Optional<Medico> medicoOptional = medicoRepository.findById(consultaDTO.medico().getId());

//        if (!consultaDTO.paciente().getAtivo()){
//            throw new Exception("Não é possível marcar consultas com pacientes inativos no sistema!");
//        }
//        if(!consultaDTO.medico().getAtivo()){
//            throw new Exception("Não é possível marcar consultas com médicos inativos no sistema!");
//        }
        if(!ValidacoesDataConsulta.isDiaUtil(consultaDTO.dataHora())) {
            System.out.println(consultaDTO.dataHora() + "\nTeste");
            throw new Exception("Dia inválido! A clínica não funciona aos domingos.");
        }
        if(!ValidacoesDataConsulta.isHorarioPermitido(consultaDTO.dataHora())){
            throw new Exception(("Só é possível marcar consultas entre 7 e 19h!"));
        }
        if(!ValidacoesDataConsulta.validarHorarioDeAntecedencia(consultaDTO.dataHora())){
            throw new Exception("A consulta deve ser marcada com pelo menos 30 minutos de antecedência!");
        }

//        Medico medico = medicoOptional.get();
//        Paciente paciente = pacienteOptional.get();
//        // Verifica se o médico já possui uma consulta agendada na mesma data e horário
//        if (consultaRepository.existsByMedicoAndDataHora(medico, consultaDTO.dataHora())) {
//            throw new Exception("O médico já possui uma consulta marcada para esse horário!");
//        }
//        // Verifica se o paciente já possui uma consulta agendada na mesma data e horário
//        if (consultaRepository.existsByPacienteAndDataHora(paciente, consultaDTO.dataHora())) {
//            throw new Exception("O paciente já possui uma consulta marcada para esse horário!");
//        }

        if (medicoOptional.isPresent() && pacienteOptional.isPresent()) {
            Consulta consulta = new Consulta();
            consulta.setId(consultaDTO.id());
            consulta.setMedico(medicoOptional.get());
            consulta.setPaciente(pacienteOptional.get());
            consulta.setDataHora(consultaDTO.dataHora());

            Consulta consultaAtualizada = consultaRepository.save(consulta);
            System.out.println(("Aobaa"));
        }
        System.out.println(("BAOBA"));
    }


    public List<ConsultaDTO> listarConsultas() {
        return consultaRepository.findAll().stream().map(ConsultaDTO::new).toList();
    }

//    public void agendarConsultaTeste(AgendarConsultaDTO agendarConsultaDTO) {
//        Optional<Paciente> pacienteOptional = pacienteRepository.findById(agendarConsultaDTO.idPaciente());
//        Optional<Medico> medicoOptional = medicoRepository.findById(agendarConsultaDTO.idMedico());
//
//        System.out.println(agendarConsultaDTO.id());
//        System.out.println(("Aobaa"));
//        //Consulta consulta = null;
//        if (medicoOptional.isPresent() && pacienteOptional.isPresent()) {
//            Consulta consulta = new Consulta();
//            consulta.setId(agendarConsultaDTO.id());
//            consulta.setMedico(medicoOptional.get());
//            consulta.setPaciente(pacienteOptional.get());
//            consulta.setDataHora(agendarConsultaDTO.dataHora());
//            //consulta.setMotivoCancelamento(consulta.getMotivoCancelamento());
//
//            Consulta consultaAtualizada = consultaRepository.save(consulta);
//            System.out.println(("Consulta agendada com sucesso!"));
//        }else
//            System.out.println(("Deu ruim!"));
//
//    }


}