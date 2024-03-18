package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion;

import lombok.*;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamenValoracionDto {

    @NotNull
    private Long idTrabajoGrados; // Cambiado a Long

    @NotNull
    private String titulo;

    @NotNull
    private String linkFormatoA;

    @NotNull
    private String linkFormatoD;

    @NotNull
    private String linkFormatoE;

    @NotNull
    private String evaluadorExterno;

    @NotNull
    private String evaluadorInterno;

    @NotNull
    private String actaNombramientoEvaluadores;

    @NotNull
    private LocalDate fechaActa;

    @NotNull
    private String linkOficioDirigidoEvaluadores;

    @NotNull
    private LocalDate fechaMaximaEvaluacion;
}
