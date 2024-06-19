package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3;

import java.time.LocalDate;
import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionCoordinadorFase3Dto {

    @NotNull
    private Long idTrabajoGrados; 

    @NotNull
    private String linkActaSustentacionPublica;

    @NotNull
    private Boolean respuestaSustentacion;

    @NotNull
    private String linkEstudioHojaVidaAcademicaGrado;

    @NotNull
    private String numeroActaFinal;

    @NotNull
    private LocalDate fechaActaFinal;

}
