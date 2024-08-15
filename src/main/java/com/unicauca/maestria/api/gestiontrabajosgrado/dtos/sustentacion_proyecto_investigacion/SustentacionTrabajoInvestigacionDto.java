package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoSustentacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.RespuestaComiteSustentacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.AnexoSustentacionDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionDto {

    @NotBlank
    private String linkFormatoF;

    @NotNull
    private String linkMonografia;

    @NotNull
    private List<AnexoSustentacionDto> anexos = new ArrayList<>();

    @NotNull
    private Long idJuradoInterno;

    @NotNull
    private Long idJuradoExterno;

    @NotNull
    private ConceptoVerificacion conceptoCoordinador;

    @NotBlank
    private String linkEstudioHojaVidaAcademica;

    private List<RespuestaComiteSustentacionDto> actaFechaRespuestaComite;

    @NotBlank
    private String linkFormatoG;

    @NotNull
    private ConceptoVerificacion juradosAceptados;

    @NotNull
    private LocalDate fechaSustentacion;

    @NotBlank
    private String numeroActaConsejo;

    @NotNull
    private LocalDate fechaActaConsejo;

    @NotBlank
    private String linkFormatoH;

    @NotBlank
    private String linkFormatoI;

    @NotBlank
    private String linkEstudioHojaVidaAcademicaGrado;

    @NotNull
    private ConceptoSustentacion respuestaSustentacion;

}
