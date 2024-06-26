package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion;

import java.time.LocalDate;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionDto {

    @NotNull
    private Long idTrabajoGrados;

    @NotBlank
    private String linkFormatoF;

    @NotBlank
    private String urlDocumentacion;

    @NotNull
    private Long idJuradoInterno;

    @NotNull
    private Long idJuradoExterno;

    private Boolean conceptoCoordinador;

    @NotBlank
    private String linkFormatoG;

    @NotBlank
    private String linkEstudioHojaVidaAcademica;

    @NotNull
    private Boolean juradosAceptados;

    @NotBlank
    private String numeroActaConsejo;

    @NotNull
    private LocalDate fechaActaConsejo;

    @NotBlank
    private String linkFormatoH;

    @NotBlank
    private String linkFormatoI;

    @NotBlank
    private String linkActaSustentacionPublica;

    @NotNull
    private Boolean respuestaSustentacion;

    @NotBlank
    private String linkEstudioHojaVidaAcademicaGrado;

    @NotBlank
    private String numeroActaFinal;

    @NotNull
    private LocalDate fechaActaFinal;

}
