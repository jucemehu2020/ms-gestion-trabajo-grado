package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente;

import java.util.ArrayList;
import java.util.List;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.AnexoSolicitudExamenValoracion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SolicitudExamenValoracionDocenteResponseDto {

    private Long idExamenValoracion;
    private String titulo;
    private String linkFormatoA;
    private String linkFormatoD;
    private String linkFormatoE;
    private List<AnexoSolicitudExamenValoracion> anexos = new ArrayList<>();
    private Long idEvaluadorInterno;
    private Long idEvaluadorExterno;
}
