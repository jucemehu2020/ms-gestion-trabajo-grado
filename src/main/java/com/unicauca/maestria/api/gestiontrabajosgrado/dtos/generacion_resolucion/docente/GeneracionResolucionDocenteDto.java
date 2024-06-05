package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente;

import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionDocenteDto {
    
    @NotNull
    private Long idTrabajoGrados;

    @NotNull
    private String director;

    @NotNull
    private String codirector;

    @NotNull
    private String linkAnteproyectoFinal;

    @NotNull
    private String linkSolicitudComite;

}
