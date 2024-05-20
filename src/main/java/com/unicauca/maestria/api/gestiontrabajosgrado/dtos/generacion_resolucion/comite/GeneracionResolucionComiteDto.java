package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.comite;

import java.time.LocalDate;
import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionComiteDto {
    
    @NotNull
    private Long idTrabajoGrados;

    @NotNull
    private String linkSolicitudConcejoFacultad;

    @NotNull
    private String numeroResolucionGeneradaCF;

    @NotNull
    private LocalDate fechaResolucion;

    @NotNull
    private String linkResolucionGeneradaCF;
}
