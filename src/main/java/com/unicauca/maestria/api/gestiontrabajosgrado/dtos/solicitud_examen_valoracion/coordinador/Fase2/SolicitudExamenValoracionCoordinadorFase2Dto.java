package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.EnvioEmailDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudExamenValoracionCoordinadorFase2Dto {

    @NotNull
    @Valid
    private List<RespuestaComiteExamenValoracionDto> actaFechaRespuestaComite;

    @NotNull
    @Valid
    private EnvioEmailDto envioEmailDto;

    private String linkOficioDirigidoEvaluadores;

    private LocalDate fechaMaximaEvaluacion;

    @Valid
    private InformacionEnvioEvaluadorDto informacionEnvioEvaluador;
}
