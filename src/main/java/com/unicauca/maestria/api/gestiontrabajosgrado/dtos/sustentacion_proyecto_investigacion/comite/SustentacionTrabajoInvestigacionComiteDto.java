package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.comite;

import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionComiteDto {

    @NotNull
    private Long idTrabajoGrados; 

    @NotNull
    private String linkRemisionDocumentoFinalCF;

}
