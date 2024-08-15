package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3;

import java.time.LocalDate;
import lombok.*;

import javax.validation.constraints.NotNull;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptoVerificacion;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionCoordinadorFase3Dto {

    @NotNull
    private ConceptoVerificacion juradosAceptados;

    @NotNull
    private String numeroActaConsejo;

    @NotNull
    private LocalDate fechaActaConsejo;

    @Builder.Default
    private String idJuradoInterno = "Sin cambios";

    @Builder.Default
    private String idJuradoExterno = "Sin cambios";

    @NotNull
    private String linkOficioConsejo;

}
