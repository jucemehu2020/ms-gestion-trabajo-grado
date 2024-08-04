package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3;

import java.time.LocalDate;
import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionCoordinadorFase3Dto {

    @NotNull
    private String numeroActaConsejo;

    @NotNull
    private LocalDate fechaActaConsejo;

    @NotNull
    private String linkOficioConsejo;

}
