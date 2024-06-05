package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2;

import java.time.LocalDate;
import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionCoordinadorFase2Dto {

    @NotNull
    private Long idTrabajoGrados; 

    @NotNull
    private String juradoExterno;

    @NotNull
    private String juradoInterno;

    @NotNull
    private String numeroActa;

    @NotNull
    private LocalDate fechaActa;



}
