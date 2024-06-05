package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1;

import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionCoordinadorFase1Dto {

    @NotNull
    private Long idTrabajoGrados; 

    @NotNull
    private String linkFormatoG;

    @NotNull
    private String linkEstudioHojaVidaAcademica;

}
