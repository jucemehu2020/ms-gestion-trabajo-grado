package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.comite;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionComiteResponseDto {
    
    private Long idGeneracionResolucion;
    private String linkSolicitudConcejoFacultad;
    private String numeroResolucionGeneradaCF;
    private LocalDate fechaResolucion;
    private String linkResolucionGeneradaCF;

}
