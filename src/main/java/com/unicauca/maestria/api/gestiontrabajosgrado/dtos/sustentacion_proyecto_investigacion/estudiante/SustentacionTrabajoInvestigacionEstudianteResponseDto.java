package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionEstudianteResponseDto {

    private Long idSustentacionTrabajoInvestigacion; 
    private String linkFormatoH;
    private String linkFormatoI;

}
