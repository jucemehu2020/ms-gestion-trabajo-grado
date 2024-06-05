package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion;

import java.time.LocalDate;
import lombok.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionDto {

    @NotNull
    private Long idTrabajoGrados;

    @NotNull
    private String linkFormatoF;

    @NotNull
    private String urlDocumentacion;

    @NotNull
    private String linkFormatoG;

    @NotNull
    private String linkEstudioHojaVidaAcademica;

    @NotNull
    private Boolean juradoExterno;

    @NotNull
    private String juradoInterno;

    @NotNull
    private String numeroActa;

    @NotNull
    private LocalDate fechaActa;

    @NotNull
    private String linkFormatoH;

    @NotNull
    private String linkFormatoI;

    @NotNull
    private String linkActaSustentacionPublica;

    @NotNull
    private Boolean respuestaSustentacion;

    @NotNull
    private String linkEstudioHojaVidaAcademicaGrado;

    @NotNull
    private String numeroActaFinal;

    @NotNull
    private LocalDate fechaActaFinal;

}
