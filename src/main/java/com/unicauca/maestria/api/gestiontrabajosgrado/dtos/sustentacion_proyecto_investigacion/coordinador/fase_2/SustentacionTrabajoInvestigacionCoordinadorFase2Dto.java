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
    private Boolean juradosAceptados;
    
    @Builder.Default
    private String idJuradoInterno = "Sin cambios";

    @Builder.Default
    private String idJuradoExterno = "Sin cambios";

    @NotNull
    private String numeroActa;

    @NotNull
    private LocalDate fechaActa;

}
