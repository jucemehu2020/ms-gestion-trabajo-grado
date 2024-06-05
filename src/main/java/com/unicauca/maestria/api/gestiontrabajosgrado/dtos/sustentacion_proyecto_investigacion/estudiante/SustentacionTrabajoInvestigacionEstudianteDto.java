package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante;

import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionEstudianteDto {

    @NotNull
    private Long idTrabajoGrados; 

    @NotNull
    private String linkFormatoH;

    @NotNull
    private String linkFormatoI;

}
