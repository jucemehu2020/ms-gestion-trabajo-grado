package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador;

import java.time.LocalDate;
import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionCoordinadorDto {
    
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

}
