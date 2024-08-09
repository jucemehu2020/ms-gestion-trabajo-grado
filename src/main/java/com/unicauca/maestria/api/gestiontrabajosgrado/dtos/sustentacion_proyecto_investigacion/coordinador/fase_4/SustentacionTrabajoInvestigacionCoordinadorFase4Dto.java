package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4;

import lombok.*;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoSustentacion;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionCoordinadorFase4Dto {

    @NotNull
    private ConceptoSustentacion respuestaSustentacion;

}
