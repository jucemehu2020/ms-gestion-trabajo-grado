package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class STICoordinadorFase1ResponseDto {

    private Long idSustentacionTI; 
    private String linkFormatoF;
    private String urlDocumentacion;
    private String linkFormatoG;
    private String linkEstudioHojaVidaAcademica;

}
