package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionCoordinadorFase2ResponseDto {

    private Long id;
    private List<RespuestaComiteGeneracionResolucionDto> actaFechaRespuestaComite;
    private String linkSolicitudConsejoFacultad;

}
