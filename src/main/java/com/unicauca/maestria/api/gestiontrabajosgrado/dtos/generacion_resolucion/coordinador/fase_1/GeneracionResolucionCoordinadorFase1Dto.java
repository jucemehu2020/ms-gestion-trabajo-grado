package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1;

import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionCoordinadorFase1Dto {
    
    @NotNull
    private Long idTrabajoGrados;

    @NotNull
    private Boolean conceptoDocumentosCoordinador;

}
