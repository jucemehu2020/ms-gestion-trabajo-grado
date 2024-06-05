package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente;

import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionDocenteDto {

    @NotNull
    private Long idTrabajoGrados;

    @NotNull
    private String linkFormatoF;

    @NotNull
    private String urlDocumentacion;

}
