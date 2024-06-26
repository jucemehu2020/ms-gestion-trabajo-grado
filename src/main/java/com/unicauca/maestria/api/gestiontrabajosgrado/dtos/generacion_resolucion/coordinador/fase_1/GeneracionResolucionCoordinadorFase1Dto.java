package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1;

import lombok.*;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionCoordinadorFase1Dto {

    @NotNull
    private Boolean conceptoDocumentosCoordinador;

    @NotNull
    private EnvioEmailDto envioEmailDto;

    private ObtenerDocumentosParaEnvioDto obtenerDocumentosParaEnvioDto;

}
