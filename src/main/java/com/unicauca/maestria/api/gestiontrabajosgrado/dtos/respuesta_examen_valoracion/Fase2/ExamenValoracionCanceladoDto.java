package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.Fase2;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamenValoracionCanceladoDto {

    @NotNull
    private Boolean estadoFinalizado;

    @NotBlank
    private String observacion;

}
