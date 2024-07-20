package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2;

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
public class SustentacionTrabajoInvestigacionCoordinadorFase2Dto {

    @NotNull
    @Valid
    private List<RespuestaComiteSustentacionDto> actaFechaRespuestaComite;

    private String linkEstudioHojaVidaAcademica;

    private String linkFormatoG;

    @NotNull
    @Valid
    private EnvioEmailDto envioEmail;

    @Valid
    private InformacionEnvioConsejoDto informacionEnvioConsejo;

}
