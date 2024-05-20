package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionCoordinadorResponseDto {

    private Long idSustentacionTI; 
    private String linkConstanciaDocumentoFinal;
    private String linkActaSustentacion;
    private String linkActaSustentacionPublica;
    private Boolean respuestaSustentacion;
    private String linkEstudioHojaVidaAcademica;
    private String numeroActaTrabajoFinal;
    private LocalDate fechaActa;

}
