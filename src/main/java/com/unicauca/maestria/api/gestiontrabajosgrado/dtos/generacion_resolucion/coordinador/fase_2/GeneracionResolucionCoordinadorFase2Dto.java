package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2;

import java.util.List;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionCoordinadorFase2Dto {

    @NotNull
    @Valid
    private List<RespuestaComiteGeneracionResolucionDto> actaFechaRespuestaComite;

    private String linkSolicitudConsejoFacultad;

    @NotNull
    @Valid
    private EnvioEmailDto envioEmail;

}
