package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolicitudExamenValoracionCoordinadorFase2ResponseDto {

    private Long id;
    private List<RespuestaComiteExamenValoracionDto> actaFechaRespuestaComite;
    private String linkOficioDirigidoEvaluadores;
    private LocalDate fechaMaximaEvaluacion;
}
