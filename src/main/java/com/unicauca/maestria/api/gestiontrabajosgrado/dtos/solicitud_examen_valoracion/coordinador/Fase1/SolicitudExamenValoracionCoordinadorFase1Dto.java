package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase1;

import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.DocumentosEnvioComiteDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SolicitudExamenValoracionCoordinadorFase1Dto {

    @NotNull
    private Boolean conceptoCoordinadorDocumentos;

    @NotNull
    private EnvioEmailDto envioEmail;
    
    private DocumentosEnvioComiteDto documentosEnvioComiteDto;
}
