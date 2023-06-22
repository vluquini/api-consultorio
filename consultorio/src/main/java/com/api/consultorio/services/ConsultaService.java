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
        ValidacoesDataConsulta validacoesDataConsulta = new ValidacoesDataConsulta();

        if(consultaDTO.medico() == null){
            validacoesDataConsulta.escolherMedicoAleatorio(medicoRepository);
        }

        if(!consultaRepository.findAll().isEmpty() &&
                validacoesDataConsulta.validarDiaConsultaPaciente(consultaDTO, consultaRepository)){
            throw new Exception("Não pode marcar consulta no mesmo dia para o mesmo paciente");
        }

        Optional<Medico> medicoOptional = validacoesDataConsulta.escolherMedicoAleatorio(medicoRepository);

        if(!consultaRepository.findAll().isEmpty() &&
            !validacoesDataConsulta.verificarMedicoConsultaMarcada(medicoOptional.get(), consultaDTO, consultaRepository)){
            throw new Exception("Medico ja possui consulta nessa horario seu bosta");
        }

        if (medicoOptional.isPresent() && pacienteOptional.isPresent()) {
            Consulta consulta = new Consulta();
            consulta.setId(consultaDTO.id());
            consulta.setMedico(medicoOptional.get());
            consulta.setPaciente(pacienteOptional.get());
            consulta.setDataHora(consultaDTO.dataHora());

            consultaRepository.save(consulta);
            System.out.println(("Aobaa"));
        }
        System.out.println(("BAOBA"));
    }

//    public void agendarConsulta(ConsultaDTO consultaDTO) throws Exception{
//        Optional<Paciente> pacienteOptional = pacienteRepository.findById(consultaDTO.paciente().getId());
//        Optional<Medico> medicoOptional = medicoRepository.findById(consultaDTO.medico().getId());
//        ValidacoesDataConsulta validacoesDataConsulta = new ValidacoesDataConsulta();
//
//        validacoesDataConsulta.escolherMedicoConsulta(consultaDTO, consultaRepository, medicoRepository)
//
//
//
//        if (!pacienteOptional.get().getAtivo()){
//            throw new Exception("Não é possível marcar consultas com pacientes inativos no sistema!");
//        }
//        if(!medicoOptional.get().getAtivo()){
//            throw new Exception("Não é possível marcar consultas com médicos inativos no sistema!");
//        }
//        //////////////////////////////////////////////////////////////////////////////
//        if (auxiliar >= 1 && validacoesDataConsulta.validarDiaConsultaPaciente(consultaDTO, consultaRepository)) {
//            throw new Exception("O paciente não pode ter mais de uma consulta no mesmo dia!");
//        }
//        auxiliar++;
//
//        if (auxiliar2 >= 1 && validacoesDataConsulta.validarDiaConsultaMedico(consultaDTO, consultaRepository)) {
//            throw new Exception("O médico já possui uma consulta marcada neste horário!");
//        }
//        auxiliar2++;
//        //////////////////////////////////////////////////////////////////////////////
//        if(!ValidacoesDataConsulta.isDiaUtil(consultaDTO.dataHora())) {
//            throw new Exception("Dia inválido! A clínica não funciona aos domingos.");
//        }
//        if(!ValidacoesDataConsulta.isHorarioPermitido(consultaDTO.dataHora())){
//            throw new Exception(("Só é possível marcar consultas entre 7h e 19h!"));
//        }
//        if(!ValidacoesDataConsulta.validarHorarioDeAntecedencia(consultaDTO.dataHora())){
//            throw new Exception("A consulta deve ser marcada com pelo menos 30 minutos de antecedência!");
//        }
//
//
//        if (medicoOptional.isPresent() && pacienteOptional.isPresent()) {
//            Consulta consulta = new Consulta();
//            consulta.setId(consultaDTO.id());
//            consulta.setMedico(medicoOptional.get());
//            consulta.setPaciente(pacienteOptional.get());
//            consulta.setDataHora(consultaDTO.dataHora());
//
//            Consulta consultaAtualizada = consultaRepository.save(consulta);
//            System.out.println(("Aobaa"));
//        }
//        System.out.println(("BAOBA"));
//    }


    public List<ConsultaDTO> listarConsultas() {
        return consultaRepository.findAll().stream().map(ConsultaDTO::new).toList();
    }

}
