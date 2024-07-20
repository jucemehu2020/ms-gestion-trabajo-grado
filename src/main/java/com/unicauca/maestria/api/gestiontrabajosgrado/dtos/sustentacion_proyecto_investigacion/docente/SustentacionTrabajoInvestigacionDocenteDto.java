package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionDocenteDto {

    @NotNull
    private String linkMonografia;

    @NotNull
    private List<AnexoSustentacionDto> anexos = new ArrayList<>();

    @NotNull
    private String linkFormatoF;

    @NotNull
    private Long idJuradoInterno;

    @NotNull
    private Long idJuradoExterno;

}
