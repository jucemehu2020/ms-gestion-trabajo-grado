package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4;

import java.time.LocalDate;

import lombok.*;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptosVarios;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionCoordinadorFase4Dto {

    @NotNull
    private ConceptosVarios respuestaSustentacion;

    private String linkActaSustentacionPublica;

    private String numeroActaFinal;

    private LocalDate fechaActaFinal;

}
