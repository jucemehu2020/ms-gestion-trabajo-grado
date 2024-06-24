package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente;

import lombok.*;

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
public class SolicitudExamenValoracionDocenteDto {

    @NotNull
    private String titulo;

    @NotNull
    private String linkFormatoA;

    @NotNull
    private String linkFormatoD;

    @NotBlank
    private String linkFormatoE;

    private List<AnexoSolicitudExamenValoracion> anexos = new ArrayList<>();

    @NotNull
    private Long idEvaluadorInterno;

    @NotNull
    private Long idEvaluadorExterno;
}
