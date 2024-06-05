package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionCoordinadorFase1ResponseDto {
    
    private Long idGeneracionResolucion;
    private String numeroActaSolicitudComite;
    private LocalDate fechaActaSolicitudComite;
    private String linkSolicitudConsejoFacultad;

}
