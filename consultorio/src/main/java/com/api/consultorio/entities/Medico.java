package com.api.consultorio.entities;

import com.api.consultorio.dtos.MedicoDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private String nome;
    @NotNull
    private String email;
    @NotNull
    private String telefone;
    @NotNull
    private String crm;
    @Enumerated(value = EnumType.STRING)
    @NotNull
    private Especialidade especialidade;
    @NotNull
    private boolean ativo;


    public Medico(MedicoDTO medicoDTO) {
        this.id = medicoDTO.id();
        this.nome = medicoDTO.nome();
        this.email = medicoDTO.email();
        this.telefone = medicoDTO.telefone();
        this.crm = medicoDTO.crm();
        this.especialidade = medicoDTO.especialidade();
        this.ativo = true;
    }
}
