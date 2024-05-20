package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SolicitudExamenValoracionCoordinadorResponseDto {

    private Long idExamenValoracion;
    private String actaAprobacionExamen;
    private LocalDate fechaActa;
    private String linkOficioDirigidoEvaluadores;
    private LocalDate fechaMaximaEvaluacion;
}
