package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase1;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolicitudExamenValoracionCoordinadorFase1Dto {

    @NotNull
    private ConceptoVerificacion conceptoCoordinadorDocumentos;

    @Valid
    private EnvioEmailDto envioEmail;

}
