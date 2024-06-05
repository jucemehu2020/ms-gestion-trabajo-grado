package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionCoordinadorFase2ResponseDto {
    
    private Long idGeneracionResolucion;
    private String numeroActaConsejoFacultad;
    private LocalDate fechaActaConsejoFacultad;

}
