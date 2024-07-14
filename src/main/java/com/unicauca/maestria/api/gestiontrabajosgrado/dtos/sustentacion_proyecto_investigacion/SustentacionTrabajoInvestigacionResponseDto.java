package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion;

import java.time.LocalDate;
import java.util.List;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptosVarios;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.RespuestaComiteSustentacionDto;

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
    private ConceptoVerificacion conceptoCoordinador;
    private List<RespuestaComiteSustentacionDto> actaFechaRespuestaComite;
    private String linkFormatoG;
    private String linkEstudioHojaVidaAcademica;
    private ConceptoVerificacion juradosAceptados;
    private String numeroActaConsejo;
    private LocalDate fechaActaConsejo;
    private String linkFormatoH;
    private String linkFormatoI;
    private String linkActaSustentacionPublica;
    private ConceptosVarios respuestaSustentacion;
    private String linkEstudioHojaVidaAcademicaGrado;
    private String numeroActaFinal;
    private LocalDate fechaActaFinal;

}
