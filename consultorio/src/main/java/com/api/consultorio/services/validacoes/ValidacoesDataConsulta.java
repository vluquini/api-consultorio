package com.api.consultorio.services.validacoes;

import com.api.consultorio.dtos.ConsultaDTO;
import com.api.consultorio.entities.consulta.Consulta;
import com.api.consultorio.entities.medico.Medico;
import com.api.consultorio.entities.paciente.Paciente;
import com.api.consultorio.repositories.ConsultaRepository;
import com.api.consultorio.repositories.MedicoRepository;
import com.api.consultorio.repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;

import java.time.*;
import java.util.List;
import java.util.Optional;


public class ValidacoesDataConsulta {
    @Autowired
    MedicoRepository medicoRepository;
    @Autowired
    PacienteRepository pacienteRepository;
//    @Autowired
//    ConsultaRepository consultaRepository;

    public static boolean isDiaUtil(LocalDateTime dataHora) {
        DayOfWeek diaSemana = dataHora.getDayOfWeek();
        // verifica se o dia está entre segunda e sábado
        return !diaSemana.equals(DayOfWeek.SUNDAY);
    }
    public static boolean isHorarioPermitido(LocalDateTime dataHora) {
        LocalTime horario = dataHora.toLocalTime();
        // verifica se o horário está entre 7h e 19h
        return horario.isAfter(LocalTime.of(7, 0)) && horario.isBefore(LocalTime.of(19, 0));
    }

    public static boolean validarHorarioDeAntecedencia(LocalDateTime dataHora){
        LocalDateTime horaAtual = LocalDateTime.now(ZoneId.systemDefault());
        // verifica se a consulta está sendo marcada com 30 min de antecedência
        return !dataHora.isBefore(horaAtual.plusMinutes(30));
    }

    // nao permite que o paciente marque mais de uma consulta no mesmo dia
    public boolean validarDiaConsultaPaciente(ConsultaDTO consultaDTO, ConsultaRepository consultaRepository){
        LocalDate diaAtual = LocalDate.now(ZoneId.systemDefault());
        List<Consulta> listaConsultas = consultaRepository.findAll();
        int i = 0;
        //System.out.println(listaConsultas);
        while (listaConsultas != null){
                System.out.println("Teste");
                return true;

        }
        return false;
    }
    /*
    public List<ConsultaDTO> listarConsultas() {
        return consultaRepository.findAll().stream().map(ConsultaDTO::new).toList();
    }
     */
    /*
    Preciso verificar se há médico no corpo da requisição;
    Se não houver, pesquisasr um médico que esteja "ativo" e esteja
    disponível no horário da consulta...

    // Verifica se o médico já possui consulta marcada para essa data/hora
        if (consultaRepository.existsByMedicoAndDataHora(consultaDTO.medico().getId(), consultaDTO.dataHora())) {
        throw new RuntimeException("Médico já possui consulta marcada para esse horário.");
    }
    // verifica se há algum médico com o id passado na requisição
        if (consultaRepository.existsById(consultaDTO.medico().getId()))
            System.out.print("Teste");

     */

}
