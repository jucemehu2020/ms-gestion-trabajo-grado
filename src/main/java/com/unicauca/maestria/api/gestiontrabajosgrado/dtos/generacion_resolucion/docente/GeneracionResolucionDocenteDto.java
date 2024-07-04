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
    private Long idDirector;

    @NotNull
    private Long idCodirector;

    @NotNull
    private String linkAnteproyectoFinal;

    @NotNull
    private String linkSolicitudComite;

}
