package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador;

import java.time.LocalDate;
import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionCoordinadorDto {

    @NotNull
    private Long idTrabajoGrados; 

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
