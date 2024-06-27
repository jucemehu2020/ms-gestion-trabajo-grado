package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.AnexoRespuestaExamenValoracion;

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
    private List<AnexoRespuestaExamenValoracion> anexos;
    private String respuestaExamenValoracion;
    private LocalDate fechaMaximaEntrega;
    private String idEvaluador;
    private String tipoEvaluador;
    
}
