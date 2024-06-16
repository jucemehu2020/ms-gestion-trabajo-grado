package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SolicitudExamenValoracionCoordinadorFase1Dto {

    @NotNull
    private Long idExamenValoracion;

    @NotBlank
    private String conceptoCoordinadorDocumentos;
}
