package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1;

import lombok.*;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionCoordinadorFase1Dto {

    @NotNull
    private Boolean conceptoCoordinador;

    @NotNull
    private EnvioEmailDto envioEmailDto;

    private ObtenerDocumentosParaEnvioDto obtenerDocumentosParaEnvioDto;

}
