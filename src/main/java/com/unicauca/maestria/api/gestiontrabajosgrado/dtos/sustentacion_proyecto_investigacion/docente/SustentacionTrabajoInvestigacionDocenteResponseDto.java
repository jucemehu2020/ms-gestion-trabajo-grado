package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionDocenteResponseDto {

    private Long id; 
    private String linkFormatoF;
    private String urlDocumentacion;
    private Long idJuradoInterno;
    private Long idJuradoExterno;

}
