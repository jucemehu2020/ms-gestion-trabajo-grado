package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.AnexoRespuestaExamenValoracion;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespuestaExamenValoracionDto {

    private Long idRespuestaExamenValoracion;

    @NotBlank
    private String linkFormatoB;

    @NotBlank
    private String linkFormatoC;

    @NotBlank
    private String linkObservaciones;

    private List<AnexoRespuestaExamenValoracion> anexos;

    @NotBlank
    private String respuestaExamenValoracion;

    private LocalDate fechaMaximaEntrega;

    @NonNull
    private String idEvaluador;

    @NonNull
    private String tipoEvaluador;

    private Boolean permitidoExamen;

    //private TrabajoGradoResponseDto trabajoGrado2;

    private InformacionEnvioDto informacionEnvioDto;

}
