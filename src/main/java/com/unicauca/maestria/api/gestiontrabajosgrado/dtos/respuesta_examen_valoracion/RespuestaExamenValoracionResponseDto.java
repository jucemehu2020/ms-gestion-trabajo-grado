package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespuestaExamenValoracionResponseDto {
    
    private Long idRespuestaExamenValoracion;
    private String linkFormatoB;
    private String linkFormatoC;
    private String linkObservaciones;
    private List<AnexoRespuestaExamenValoracionDto> anexos;
    private String respuestaExamenValoracion;
    private LocalDate fechaMaximaEntrega;
    private Long idEvaluador;
    private String tipoEvaluador;
    
}
