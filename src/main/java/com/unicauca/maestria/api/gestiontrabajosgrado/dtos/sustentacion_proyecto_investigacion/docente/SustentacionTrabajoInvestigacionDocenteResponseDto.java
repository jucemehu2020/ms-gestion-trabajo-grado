package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente;

import java.util.Map;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionDocenteResponseDto {

    private Long idSustentacionTI; 
    private String linkFormatoF;
    private String urlDocumentacion;
    private Map<String, String> juradoInterno;
    private Map<String, String> juradoExterno;

}
