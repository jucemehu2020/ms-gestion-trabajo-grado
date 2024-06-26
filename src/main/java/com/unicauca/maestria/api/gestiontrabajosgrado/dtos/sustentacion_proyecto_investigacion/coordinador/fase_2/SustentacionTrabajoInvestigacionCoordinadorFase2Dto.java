package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2;

import java.util.List;

import lombok.*;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.RespuestaComiteGeneracionResolucionDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionCoordinadorFase2Dto {

    @NotNull
    private List<RespuestaComiteGeneracionResolucionDto> actaFechaRespuestaComite;

    @NotNull
    private String linkEstudioHojaVidaAcademica;

    @NotNull
    private String linkFormatoG;

    @NotNull
    private EnvioEmailDto envioEmailDto;

}
