package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionResponseDto {
    
    private Long idGeneracionResolucion;
    private String titulo;
    private String director;
    private String codirector;
    private String numeroActaRevision;
    private LocalDate fechaActa;
    private String linkAnteproyectoAprobado;
    private String linkSolicitudComite;
    private String linkSolicitudConcejoFacultad;
    private String numeroResolucionGeneradaCF;
    private LocalDate fechaResolucion;
    private String linkResolucionGeneradaCF;
}
