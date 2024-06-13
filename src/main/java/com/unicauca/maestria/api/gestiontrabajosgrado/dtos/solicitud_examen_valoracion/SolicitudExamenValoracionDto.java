package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion;

import lombok.*;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudExamenValoracionDto {

    @NotNull
    private Long idTrabajoGrados; 

    @NotNull
    private String titulo;

    @NotNull
    private String linkFormatoA;

    @NotNull
    private String linkFormatoD;

    @NotBlank
    private String linkFormatoE;

    private String linkAnexos;

    @NotNull
    private String evaluadorExterno;

    @NotNull
    private String evaluadorInterno;

    @NotNull
    private String actaAprobacionExamen;

    @NotNull
    private LocalDate fechaActa;

    @NotNull
    private String linkOficioDirigidoEvaluadores;

    @NotNull
    private LocalDate fechaMaximaEvaluacion;
}
