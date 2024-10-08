package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante;

import lombok.*;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionEstudianteDto {

    @NotNull
    private String linkFormatoH;

    @NotNull
    private String linkFormatoI;

    @NotNull
    private String linkEstudioHojaVidaAcademicaGrado;

    @NotNull
    private LocalDate fechaSustentacion;

}
