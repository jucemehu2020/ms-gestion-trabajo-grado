package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2;

import java.time.LocalDate;
import java.util.Map;

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
    private Map<String, String> juradoInterno;
    private Map<String, String> juradoExterno;
    private String numeroActa;
    private LocalDate fechaActa;

}
