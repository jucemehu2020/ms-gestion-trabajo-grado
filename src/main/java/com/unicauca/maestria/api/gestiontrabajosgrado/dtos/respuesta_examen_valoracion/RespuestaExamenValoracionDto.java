package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.respuesta_examen_valoracion.ConceptoRespuesta;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.respuesta_examen_valoracion.TipoEvaluador;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespuestaExamenValoracionDto {

    @NotNull
    private String linkFormatoB;

    @NotNull
    private String linkFormatoC;

    @NotNull
    private String linkObservaciones;

    @Valid
    private List<AnexoRespuestaExamenValoracionDto> anexos;

    @NotNull
    private ConceptoRespuesta respuestaExamenValoracion;

    private LocalDate fechaMaximaEntrega;

    @NotNull
    private Long idEvaluador;

    @NotNull
    private TipoEvaluador tipoEvaluador;

    private Boolean permitidoExamen;

    @Valid
    private EnvioEmailDto envioEmail;

}
