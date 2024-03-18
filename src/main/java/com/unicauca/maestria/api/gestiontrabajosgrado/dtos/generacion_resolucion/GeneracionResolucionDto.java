package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion;

import java.time.LocalDate;
import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionDto {
    
    @NotNull
    private Long idTrabajoGrados;

    @NotNull
    private String titulo;

    @NotNull
    private String director;

    @NotNull
    private String codirector;

    @NotNull
    private String numeroActaRevision;

    @NotNull
    private LocalDate fechaActa;

    @NotNull
    private String linkAnteproyectoAprobado;

    @NotNull
    private String linkSolicitudComite;

    @NotNull
    private String linkSolicitudConcejoFacultad;

    //CF: Concejo de facultad
    @NotNull
    private String numeroResolucionGeneradaCF;

    @NotNull
    private LocalDate fechaResolucion;

    @NotNull
    private String linkResolucionGeneradaCF;
}
