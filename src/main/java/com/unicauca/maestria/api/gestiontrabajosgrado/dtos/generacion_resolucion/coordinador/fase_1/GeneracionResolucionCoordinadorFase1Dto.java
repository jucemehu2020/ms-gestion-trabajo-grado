package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1;

import java.time.LocalDate;
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
    private String numeroActaSolicitudComite;

    @NotNull
    private LocalDate fechaActaSolicitudComite;

    @NotNull
    private String linkSolicitudConsejoFacultad;

}
