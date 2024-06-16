package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.RespuestaComite;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudExamenValoracionCoordinadorFase2Dto {

    @NotNull
    private Long idTrabajoGrados;

    @NotNull
    private List<RespuestaComite> actaFechaRespuestaComite;

    @NonNull
    private EnvioEmailDto envioEmailDto;

    private String linkOficioDirigidoEvaluadores;

    private LocalDate fechaMaximaEvaluacion;

    private InformacionEnvioEvaluadorDto informacionEnvioEvaluador;
}
