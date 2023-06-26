package com.api.consultorio.services.validacoes;

import com.api.consultorio.entities.consulta.MotivoCancelamento;

import java.time.Duration;
/*
Est classe contém os métodos para validar o cancelamento
de uma consulta.
 */
public class ValidacoesCancelamento {

    public boolean verificarMotivo(MotivoCancelamento motivo){
        return motivo != null;
    }

    public void validarMotivoCancelamento(MotivoCancelamento motivo) throws Exception {
        if (motivo == null) {
            throw new Exception("É necessário informar o motivo do cancelamento!");
        }
    }

    public void validarIdConsulta(Long id) throws Exception {
        if (id == null) {
            throw new Exception("É necessário informar o ID da consulta a ser cancelada!");
        }
    }

    public void validarAntecedenciaCancelamento(Duration diferenca) throws Exception {
        if (diferenca.toHours() < 24) {
            throw new Exception("A consulta deve ser cancelada com antecedência mínima de 24 horas.");
        }
    }
}
