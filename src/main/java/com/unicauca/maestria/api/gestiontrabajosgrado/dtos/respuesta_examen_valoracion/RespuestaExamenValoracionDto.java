package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion;

import lombok.*;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.InformacionEnvioEvaluador;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespuestaExamenValoracionDto {

    private Long idRtaExamenValoracion;

    @NotNull
    private Long idTrabajoGrados;

    @NotBlank
    private String linkFormatoB;

    @NotBlank
    private String linkFormatoC;

    @NotBlank
    private String linkObservaciones;

    @NotBlank
    private String respuestaExamenValoracion;

    private LocalDate fechaMaximaEntrega;

    private Boolean estadoFinalizado;

    private String observacion;

    @NonNull
    private String idEvaluador;

    @NonNull
    private String tipoEvaluador;

    private TrabajoGradoResponseDto trabajoGrado;

    @NotNull
    private InformacionEnvioDto informacionEnvioDto;

}
