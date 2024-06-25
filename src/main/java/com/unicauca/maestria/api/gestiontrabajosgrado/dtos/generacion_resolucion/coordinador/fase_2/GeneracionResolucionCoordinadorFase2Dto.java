package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2;

import java.util.List;

import lombok.*;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionCoordinadorFase2Dto {

    @NotNull
    private List<RespuestaComiteGeneracionResolucionDto> actaFechaRespuestaComite;

    @NotNull
    private String linkSolicitudConsejoFacultad;

    @NotNull
    private EnvioEmailDto envioEmailDto;

}
