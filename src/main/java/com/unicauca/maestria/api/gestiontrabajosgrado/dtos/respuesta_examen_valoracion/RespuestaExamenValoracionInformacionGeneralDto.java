package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespuestaExamenValoracionInformacionGeneralDto {
    
    private Long idTrabajoGrado;

    private String tituloTrabajoGrado;

    private Long idEvaluadorInterno;

    private String nombreEvaluadorInterno;

    private String universidadEvaluadorInterno;

    private Long idEvaluadorExterno;

    private String nombreEvaluadorExterno;

    private String universidadEvaluadorExterno;
}
