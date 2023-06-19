package com.api.consultorio.entities.medico;

import com.api.consultorio.dtos.MedicoDTO;
import com.api.consultorio.entities.Endereco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "medico")
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String crm;
    @Enumerated(value = EnumType.STRING)
    private Especialidade especialidade;
    @Embedded
    private Endereco endereco;
    private Boolean ativo;


    public Medico(MedicoDTO medicoDTO) {
        this.id = medicoDTO.id();
        this.nome = medicoDTO.nome();
        this.email = medicoDTO.email();
        this.telefone = medicoDTO.telefone();
        this.crm = medicoDTO.crm();
        this.especialidade = medicoDTO.especialidade();
        this.endereco = medicoDTO.endereco();
        this.ativo = true;
    }
}
