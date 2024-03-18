package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion;

import java.time.LocalDate;
import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionDto {

    @NotNull
    private Long idTrabajoGrados; 

    @NotNull
    private String linkRemisionDocumentoFinal;

    @NotNull
    private String urlDocumentacion;

    // CF: Concejo de facultad
    @NotNull
    private String linkRemisionDocumentoFinalCF;

    @NotNull
    private String linkConstanciaDocumentoFinal;

    @NotNull
    private String linkActaSustentacion;

    @NotNull
    private String linkActaSustentacionPublica;

    @NotNull
    private Boolean respuestaSustentacion;

    @NotNull
    private String linkEstudioHojaVidaAcademica;

    @NotNull
    private String numeroActaTrabajoFinal;

    @NotNull
    private LocalDate fechaActa;

}
