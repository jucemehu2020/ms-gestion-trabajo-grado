package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SustentacionTrabajoInvestigacionEstudianteResponseDto {

    private Long id; 
    private String linkFormatoH;
    private String linkFormatoI;
    private String linkEstudioHojaVidaAcademicaGrado;

}
