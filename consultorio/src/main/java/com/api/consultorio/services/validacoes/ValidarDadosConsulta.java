package com.api.consultorio.services.validacoes;

import com.api.consultorio.dtos.ConsultaDTO;
import com.api.consultorio.entities.consulta.Consulta;
import com.api.consultorio.entities.medico.Medico;
import com.api.consultorio.repositories.ConsultaRepository;
import com.api.consultorio.repositories.MedicoRepository;

import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/*
Esta classe contém métodos que validam os dados
para o agendamento de uma consulta. Os métodos desta
classe são chamados na classe "ValidarConsulta".
 */
public class ValidarDadosConsulta {

    public boolean isDiaUtil(LocalDateTime dataHora) {
        DayOfWeek diaSemana = dataHora.getDayOfWeek();
        // verifica se o dia está entre segunda e sábado
        return !diaSemana.equals(DayOfWeek.SUNDAY);
    }
    public boolean isHorarioPermitido(LocalDateTime dataHora) {
        LocalTime horario = dataHora.toLocalTime();
        // verifica se o horário está entre 7h e 19h
        return horario.isAfter(LocalTime.of(7, 0)) && horario.isBefore(LocalTime.of(19, 0));
    }

    public boolean validarHorarioDeAntecedencia(LocalDateTime dataHora){
        LocalDateTime horaAtual = LocalDateTime.now(ZoneId.systemDefault());
        // verifica se a consulta está sendo marcada com 30 min de antecedência
        return !dataHora.isBefore(horaAtual.plusMinutes(30));
    }

    // nao permite que o paciente marque mais de uma consulta no mesmo dia
    public boolean validarDiaConsultaPaciente(ConsultaDTO consultaDTO, ConsultaRepository consultaRepository){
        List<Consulta> listaConsultas = consultaRepository.findAll();

        for (Consulta consulta : listaConsultas) {
            // Verifica apenas as consultas no mesmo dia com status de cancelada igual a "false"
            if (consulta.getPaciente().getId() == consultaDTO.paciente().getId() && !consulta.getCancelada()) {
                LocalDateTime dataHoraConsulta = consulta.getDataHora();
                LocalDate dataConsulta = dataHoraConsulta.toLocalDate();
                LocalDate dataConsultaDTO = consultaDTO.dataHora().toLocalDate();

                if (dataConsulta.isEqual(dataConsultaDTO)) {
                    return true; // Já existe consulta marcada para esse paciente no mesmo dia
                }
            }
        }
        return false; // Não há consulta marcada para esse paciente no mesmo dia
    }

    public Optional<Medico> escolherMedicoAleatorio(MedicoRepository medicoRepository) {
        // Cria uma lista com todos os médicos "ativos" cadastrados.
        List<Medico> medicosDisponiveis = medicoRepository.findAll().stream()
                .filter(Medico::getAtivo)
                .toList();
//       List<Medico> medicosDisponiveis = medicoRepository.findAll().stream()
//              .filter(medico -> medico.getAtivo())
//              .collect(Collectors.toList());

            Random random = new Random();
            long id = random.nextInt(medicosDisponiveis.size());
            Optional<Medico> medicoOptional = medicoRepository.findById(medicosDisponiveis.get((int) id).getId());
            return medicoOptional;

    }

    public boolean verificarMedicoConsultaMarcada(Medico medico, ConsultaDTO consultaDTO,
                                                  ConsultaRepository consultaRepository){
        return consultaRepository.existsByMedicoAndDataHoraAndCanceladaFalse(medico,
                consultaDTO.dataHora());
    }

    // Verifica se o medico já possui uma consulta marcada para este horário
//    public boolean verificarMedicoConsultaMarcada(Medico medico, ConsultaDTO consultaDTO,
//                                                  ConsultaRepository consultaRepository){
//        return consultaRepository.existsByMedicoAndDataHora(medico,
//                consultaDTO.dataHora());
//    }


}
