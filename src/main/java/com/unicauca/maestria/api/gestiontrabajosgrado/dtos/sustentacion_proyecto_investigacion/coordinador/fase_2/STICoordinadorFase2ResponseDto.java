package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class STICoordinadorFase2ResponseDto {

    private Long idSustentacionTI;
    private String linkFormatoF;
    private String urlDocumentacion;
    private String linkFormatoG;
    private String linkEstudioHojaVidaAcademica;
    private String juradoExterno;
    private String juradoInterno;
    private String numeroActa;
    private LocalDate fechaActa;

}
