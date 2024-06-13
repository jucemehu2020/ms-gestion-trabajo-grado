package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SolicitudExamenValoracionResponseDto {

    private Long idExamenValoracion;
    private String titulo;
    private String linkFormatoA;
    private String linkFormatoD;
    private String linkFormatoE;
    private String linkAnexos;
    private String evaluadorExterno;
    private String evaluadorInterno;
    private String actaAprobacionExamen;
    private LocalDate fechaActa;
    private String linkOficioDirigidoEvaluadores;
    private LocalDate fechaMaximaEvaluacion;
}
