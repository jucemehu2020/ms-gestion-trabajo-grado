package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class STICoordinadorFase2ResponseDto {

    private Long idSustentacionTrabajoInvestigacion;
    private List<RespuestaComiteSustentacionDto> actaFechaRespuestaComite;
    private String linkEstudioHojaVidaAcademica;
    private String linkFormatoG;

}
