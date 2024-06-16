package com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "anexos_respuesta_examen_valoracion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnexoRespuestaExamenValoracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String linkAnexo;

    @ManyToOne
    @JoinColumn(name = "id_respuesta_examen_valoracion")
    private RespuestaExamenValoracion respuestaExamenValoracion;
}
