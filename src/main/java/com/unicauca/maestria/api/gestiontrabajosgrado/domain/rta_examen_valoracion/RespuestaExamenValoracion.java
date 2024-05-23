package com.unicauca.maestria.api.gestiontrabajosgrado.domain.rta_examen_valoracion;

import java.time.LocalDate;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rta_examen_valoracion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespuestaExamenValoracion {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idRtaExamenValoracion;

    private String linkFormatoB;

    private String linkFormatoC;

    private String linkObservaciones;

    private String respuestaExamenValoracion;

    private LocalDate fechaMaximaEntrega;

    private String idEvaluador;

    private String tipoEvaluador;

    private Boolean estadoFinalizado;

    private String observacion;

    @ManyToOne
    @JoinColumn(name = "id_trabajo_grado")
    private TrabajoGrado trabajoGrado;

}
