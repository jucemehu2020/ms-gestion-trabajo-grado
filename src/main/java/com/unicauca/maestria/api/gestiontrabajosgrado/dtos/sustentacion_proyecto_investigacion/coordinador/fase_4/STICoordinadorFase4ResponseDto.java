package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class STICoordinadorFase4ResponseDto {

    private Long idSustentacionTI;
    private String linkActaSustentacionPublica;
    private Boolean respuestaSustentacion;
    private String linkEstudioHojaVidaAcademicaGrado;
    private String numeroActaFinal;
    private LocalDate fechaActaFinal;

}
