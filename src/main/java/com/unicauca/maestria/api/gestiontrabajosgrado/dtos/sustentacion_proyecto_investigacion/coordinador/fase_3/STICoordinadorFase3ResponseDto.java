package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3;

import java.time.LocalDate;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class STICoordinadorFase3ResponseDto {

    private Long idSustentacionTrabajoInvestigacion;
    private ConceptoVerificacion juradosAceptados;
    private String numeroActaConsejo;
    private LocalDate fechaActaConsejo;
    private String idJuradoInterno;
    private String idJuradoExterno;

}
