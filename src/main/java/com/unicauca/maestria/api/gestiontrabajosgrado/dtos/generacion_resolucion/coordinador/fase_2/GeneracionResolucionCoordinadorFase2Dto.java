package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2;

import java.time.LocalDate;
import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionCoordinadorFase2Dto {
    
    @NotNull
    private Long idTrabajoGrados;

    @NotNull
    private String numeroActaConsejoFacultad;

    @NotNull
    private LocalDate fechaActaConsejoFacultad;

}
