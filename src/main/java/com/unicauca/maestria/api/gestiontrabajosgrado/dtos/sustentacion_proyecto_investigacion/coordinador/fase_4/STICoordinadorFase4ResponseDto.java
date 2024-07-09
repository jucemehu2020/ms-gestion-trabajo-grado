package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4;

import java.time.LocalDate;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptosVarios;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class STICoordinadorFase4ResponseDto {

    private Long id;
    private String linkActaSustentacionPublica;
    private ConceptosVarios respuestaSustentacion;
    private String linkEstudioHojaVidaAcademicaGrado;
    private String numeroActaFinal;
    private LocalDate fechaActaFinal;

}
