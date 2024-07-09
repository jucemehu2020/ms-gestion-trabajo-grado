package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionResponseDto {

    private Long id;
    private String linkFormatoF;
    private String urlDocumentacion;
    private Long idJuradoInterno;
    private Long idJuradoExterno;
    private String linkFormatoG;
    private String linkEstudioHojaVidaAcademica;
    private Boolean juradosAceptados;
    private String numeroActa;
    private LocalDate fechaActa;
    private String linkFormatoH;
    private String linkFormatoI;
    private String linkActaSustentacionPublica;
    private Boolean respuestaSustentacion;
    private String linkEstudioHojaVidaAcademicaGrado;
    private String numeroActaFinal;
    private LocalDate fechaActaFinal;

}
