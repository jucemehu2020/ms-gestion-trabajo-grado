package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.EnvioEmailDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudExamenValoracionCoordinadorFase2Dto {

    @NotNull
    private List<RespuestaComiteDto> actaFechaRespuestaComite;

    @NotNull
    private EnvioEmailDto envioEmailDto;

    private String linkOficioDirigidoEvaluadores;

    private LocalDate fechaMaximaEvaluacion;

    private InformacionEnvioEvaluadorDto informacionEnvioEvaluador;
}
