package com.api.consultorio.entities.paciente;

import com.api.consultorio.dtos.PacienteDTO;
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
@Entity(name = "paciente")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    @Embedded
    Endereco endereco;
    Boolean ativo;

    public Paciente(PacienteDTO pacienteDTO) {
        this.id = pacienteDTO.id();
        this.nome = pacienteDTO.nome();
        this.email = pacienteDTO.email();
        this.telefone = pacienteDTO.telefone();
        this.cpf = pacienteDTO.cpf();
        this.endereco = pacienteDTO.endereco();
        this.ativo = true;
    }
}
