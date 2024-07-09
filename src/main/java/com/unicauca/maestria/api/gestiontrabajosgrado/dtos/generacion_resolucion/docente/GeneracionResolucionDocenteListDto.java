package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente;

import java.util.Map;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionDocenteListDto {

    private Long id;
    private String titulo;
    private Map<String, String> director;
    private Map<String, String> codirector;
    private String linkAnteproyectoFinal;
    private String linkSolicitudComite;

}
