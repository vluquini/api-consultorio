package com.api.consultorio.entities.consulta;

import com.api.consultorio.dtos.ConsultaDTO;
import com.api.consultorio.entities.medico.Medico;
import com.api.consultorio.entities.paciente.Paciente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "consulta")
@EqualsAndHashCode(of = "id")
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_medico")
    private Medico medico;
    @ManyToOne
    @JoinColumn(name = "id_paciente")
    private Paciente paciente;
    private LocalDateTime dataHora;
    @Column(name = "motivo_cancelamento")
    @Enumerated(EnumType.STRING)
    private MotivoCancelamento motivoCancelamento;
    private Boolean cancelada;


    public Consulta (ConsultaDTO consultaDTO){
        this(consultaDTO.id(), consultaDTO.medico(), consultaDTO.paciente(),
                consultaDTO.dataHora(), null, false);
    }

    public void cancelarConsulta(MotivoCancelamento motivo){
        this.motivoCancelamento = motivo;
        this.cancelada = true;
    }

}
