package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador;

import lombok.*;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudExamenValoracionCoordinadorDto {

    @NotNull
    private Long idTrabajoGrados; 

    @NotNull
    private String actaAprobacionExamen;

    @NotNull
    private LocalDate fechaActa;

    @NotNull
    private String linkOficioDirigidoEvaluadores;

    @NotNull
    private LocalDate fechaMaximaEvaluacion;
}
