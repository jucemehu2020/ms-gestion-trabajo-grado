package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class STICoordinadorFase2ResponseDto {

    private Long id;
    private List<RespuestaComiteSustentacionDto> actaFechaRespuestaComite;
    private String linkFormatoG;

}
