package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.comite;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionComiteResponseDto {

    private Long idSustentacionTI; 
    private String linkRemisionDocumentoFinalCF;

}
