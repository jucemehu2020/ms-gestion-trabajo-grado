package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente;

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
    private String linkAnexos;
    private String evaluadorExterno;
    private String evaluadorInterno;
}
