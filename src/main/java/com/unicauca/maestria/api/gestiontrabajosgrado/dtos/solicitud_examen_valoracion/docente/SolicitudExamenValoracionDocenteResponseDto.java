package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolicitudExamenValoracionDocenteResponseDto {

    private Long id;
    private String titulo;
    private String linkFormatoA;
    private String linkFormatoD;
    private String linkFormatoE;
    private List<AnexoSolicitudExamenValoracionDto> anexos = new ArrayList<>();
    private Long idEvaluadorInterno;
    private Long idEvaluadorExterno;
}
