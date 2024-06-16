package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.AnexoSolicitudExamenValoracion;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudExamenValoracionDto {

    @NotNull
    private Long idTrabajoGrados; 

    @NotNull
    private String titulo;

    @NotNull
    private String linkFormatoA;

    @NotNull
    private String linkFormatoD;

    @NotBlank
    private String linkFormatoE;

    private List<AnexoSolicitudExamenValoracion> anexos = new ArrayList<>();;

    @NotNull
    private String idEvaluadorInterno;

    @NotNull
    private String idEvaluadorExterno;

    @NotNull
    private String actaAprobacionExamen;

    @NotNull
    private LocalDate fechaActa;

    @NotNull
    private String linkOficioDirigidoEvaluadores;

    @NotNull
    private LocalDate fechaMaximaEvaluacion;
}
