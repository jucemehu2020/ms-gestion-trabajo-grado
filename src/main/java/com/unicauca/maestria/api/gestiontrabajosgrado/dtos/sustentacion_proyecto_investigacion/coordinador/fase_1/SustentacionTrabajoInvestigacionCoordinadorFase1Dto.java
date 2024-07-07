package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionCoordinadorFase1Dto {

    @NotNull
    private ConceptoVerificacion conceptoCoordinador;

    @NotNull
    @Valid
    private EnvioEmailDto envioEmail;

    @Valid
    private ObtenerDocumentosParaEnvioDto obtenerDocumentosParaEnvio;

}
