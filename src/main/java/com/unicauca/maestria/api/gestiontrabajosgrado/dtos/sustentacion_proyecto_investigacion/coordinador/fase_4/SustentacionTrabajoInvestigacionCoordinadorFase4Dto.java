package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionCoordinadorFase4Dto {

    @NotNull
    private String respuestaSustentacion;

    private String linkActaSustentacionPublica;

    private String linkEstudioHojaVidaAcademicaGrado;

    private String numeroActaFinal;

    private LocalDate fechaActaFinal;

    public boolean validarUnSoloAtributo() {
        List<String> atributos = Arrays.asList(respuestaSustentacion, linkActaSustentacionPublica,
                linkEstudioHojaVidaAcademicaGrado, numeroActaFinal, fechaActaFinal.toString());
        long count = atributos.stream().filter(atributo -> atributo != null && !atributo.isEmpty()).count();
        return count == 1;
    }

}
