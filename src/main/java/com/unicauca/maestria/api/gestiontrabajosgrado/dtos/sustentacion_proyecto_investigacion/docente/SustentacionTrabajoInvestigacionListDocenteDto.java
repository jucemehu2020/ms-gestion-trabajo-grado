package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionListDocenteDto {

    private Long id;
    private String linkFormatoF;
    private String linkMonografia;
    private List<String> anexos = new ArrayList<>();
    private String urlDocumentacion;
    private Map<String, String> juradoInterno;
    private Map<String, String> juradoExterno;

}
