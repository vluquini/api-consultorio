package com.api.consultorio.services;
/*
    Classe responsável por Agendar e Cancelar consultas.
 */
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
/*
    private boolean isDiaUtil(LocalDateTime dataHora) {
        DayOfWeek diaSemana = dataHora.getDayOfWeek();
        // verifica se o dia está entre segunda e sábado
        return !diaSemana.equals(DayOfWeek.SUNDAY) && !diaSemana.equals(DayOfWeek.SATURDAY);
    }

    private boolean isHorarioPermitido(LocalDateTime dataHora) {
        LocalTime horario = dataHora.toLocalTime();
        // verifica se o horário está entre 7h e 19h
        return horario.isAfter(LocalTime.of(7, 0)) && horario.isBefore(LocalTime.of(19, 0));
    }
*/

    public void agendarConsulta(ConsultaDTO consultaDTO) {
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(consultaDTO.paciente().getId());
        Optional<Medico> medicoOptional = medicoRepository.findById(consultaDTO.medico().getId());

        System.out.println(("Aobaa"));
        //Consulta consulta = null;
        if (medicoOptional.isPresent() && pacienteOptional.isPresent()) {
            Consulta consulta = new Consulta();
            consulta.setId(consultaDTO.id());
            consulta.setMedico(medicoOptional.get());
            consulta.setPaciente(pacienteOptional.get());
            consulta.setDataHora(consulta.getDataHora());
            //consulta.setMotivoCancelamento(consulta.getMotivoCancelamento());

            Consulta consultaAtualizada = consultaRepository.save(consulta);
            System.out.println(("Aobaa"));
        }

        System.out.println(("BAOBA"));
        //return new ConsultaDTO(consultaAtualizada);
    }

    public List<ConsultaDTO> listar() {
        return consultaRepository.findAll().stream().map(ConsultaDTO::new).toList();
    }


}
