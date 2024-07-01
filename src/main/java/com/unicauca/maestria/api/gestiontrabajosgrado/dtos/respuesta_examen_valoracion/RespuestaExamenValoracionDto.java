package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespuestaExamenValoracionDto {

    @NotBlank
    private String linkFormatoB;

    @NotBlank
    private String linkFormatoC;

    @NotBlank
    private String linkObservaciones;

    @Valid
    private List<AnexoRespuestaExamenValoracionDto> anexos;

    @NotBlank
    private String respuestaExamenValoracion;

    private LocalDate fechaMaximaEntrega;

    @NonNull
    private Long idEvaluador;

    @NonNull
    private String tipoEvaluador;

    private Boolean permitidoExamen;

    @Valid
    private EnvioEmailDto envioEmail;

}
