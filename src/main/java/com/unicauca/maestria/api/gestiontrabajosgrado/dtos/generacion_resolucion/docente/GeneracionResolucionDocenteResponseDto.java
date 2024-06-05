package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionDocenteResponseDto {
    
    private Long idGeneracionResolucion;
    private String titulo;
    private String director;
    private String codirector;
    private String linkAnteproyectoFinal;
    private String linkSolicitudComite;

}
