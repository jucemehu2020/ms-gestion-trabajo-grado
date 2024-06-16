package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.AnexoRespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespuestaExamenValoracionDto {

    private Long idRespuestaExamenValoracion;

    @NotNull
    private Long idTrabajoGrados;

    @NotBlank
    private String linkFormatoB;

    @NotBlank
    private String linkFormatoC;

    @NotBlank
    private String linkObservaciones;

    private List<AnexoRespuestaExamenValoracion> linkAnexo;

    @NotBlank
    private String respuestaExamenValoracion;

    private LocalDate fechaMaximaEntrega;

    @NonNull
    private String idEvaluador;

    @NonNull
    private String tipoEvaluador;

    private Boolean permitidoExamen;

    private Boolean estadoFinalizado;

    private String observacion;

    private TrabajoGradoResponseDto trabajoGrado;

    @NotNull
    private InformacionEnvioDto informacionEnvioDto;

}
