package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.Cancelado;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamenValoracionCanceladoDto {

    @NotNull
    private String observacion;

}
