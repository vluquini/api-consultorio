package com.api.consultorio.entities;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Endereco {
    private String logradouro;
    private String bairro;
    private String cep;
    private String numero;
    private String complemento;
    private String cidade;
    private String uf;

    public Endereco(EnderecoDTO enderecoDTO) {
        this.logradouro = enderecoDTO.logradouro();
        this.bairro = enderecoDTO.bairro();
        this.cep = enderecoDTO.cep();
        this.uf = enderecoDTO.uf();
        this.cidade = enderecoDTO.cidade();
        this.numero = enderecoDTO.numero();
        this.complemento = enderecoDTO.complemento();
    }

    //    public void atualizarDados(EnderecoDTO enderecoDTO) {
//        if (enderecoDTO.logradouro() != null) {
//            this.logradouro = enderecoDTO.logradouro();
//        }
//        if (enderecoDTO.bairro() != null) {
//            this.bairro = enderecoDTO.bairro();
//        }
//        if (enderecoDTO.cep() != null) {
//            this.cep = enderecoDTO.cep();
//        }
//        if (enderecoDTO.uf() != null) {
//            this.uf = enderecoDTO.uf();
//        }
//        if (enderecoDTO.cidade() != null) {
//            this.cidade = enderecoDTO.cidade();
//        }
//        this.numero = enderecoDTO.numero();
//        this.complemento = enderecoDTO.complemento();
//    }
}
