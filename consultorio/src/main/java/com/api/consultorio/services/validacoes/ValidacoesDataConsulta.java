package com.api.consultorio.services.validacoes;

import com.api.consultorio.repositories.MedicoRepository;
import com.api.consultorio.repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;


class ValidacoesDataConsulta {

    // valida dia da semana da consulta
    private static boolean isDiaUtil(LocalDateTime dataHora) {
        DayOfWeek diaSemana = dataHora.getDayOfWeek();
        // verifica se o dia está entre segunda e sábado
        return !diaSemana.equals(DayOfWeek.SUNDAY) && !diaSemana.equals(DayOfWeek.SATURDAY);
    }

    // valida hora da consulta
    private static boolean isHorarioPermitido(LocalDateTime dataHora) {
        LocalTime horario = dataHora.toLocalTime();
        // verifica se o horário está entre 7h e 19h
        return horario.isAfter(LocalTime.of(7, 0)) && horario.isBefore(LocalTime.of(19, 0));
    }

    private static boolean validarHorarioDeAntecedencia(LocalDateTime dataHora){
        LocalDateTime horaAtual = LocalDateTime.now(ZoneId.systemDefault());
        if(!dataHora.isBefore(horaAtual.plusMinutes(30)))
            return false;
        return true;
    }

    /*
    Preciso verificar se há médico no corpo da requisição;
    Se não houver, pesquisasr um médico que esteja "ativo" e esteja
    disponível no horário da consulta...

    LocalDateTime horaAtual = LocalDateTime.now(ZoneId.systemDefault());

    // Verifica se o médico já possui consulta marcada para essa data/hora
        if (consultaRepository.existsByMedicoAndDataHora(consultaDTO.medico().getId(), consultaDTO.dataHora())) {
        throw new RuntimeException("Médico já possui consulta marcada para esse horário.");
    }
    // verifica se há algum médico com o id passado na requisição
        if (consultaRepository.existsById(consultaDTO.medico().getId()))
            System.out.print("Teste");

    // se a consulta for menor que 30 minutos do horário atual...
        if(consultaDTO.dataHora().isBefore(horaAtual.plusMinutes(30))){
        throw  new Exception("Você deve agendar a consulta");
    }
        if (!isDiaUtil(consultaDTO.dataHora())) {
        throw new Exception("Data inválida: a clínica não funciona aos domingos.");
    }
        if (!isHorarioPermitido(consultaDTO.dataHora())) {
        throw new Exception("Horário inválido: a clínica funciona das 7h às 19h.");
    }
    // validação de consultas com médicos e pacientes inativos
        if(!consultaDTO.paciente().isAtivo()){
        throw new Exception("Não é possível marcar consultas com pacientes inativos.");
    }
        if(!consultaDTO.medico().isAtivo()){
        throw new Exception("Não é possível marcar consultas com médicos inativos.");
    }
     */



}
