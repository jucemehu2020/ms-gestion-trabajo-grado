package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionDocenteResponseDto {
    
    private Long id;
    private String titulo;
    private Long director;
    private Long codirector;
    private String linkAnteproyectoFinal;
    private String linkSolicitudComite;

}
