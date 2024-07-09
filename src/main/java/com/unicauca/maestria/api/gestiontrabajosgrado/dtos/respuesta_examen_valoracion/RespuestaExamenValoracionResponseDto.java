package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.ConceptosVarios;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.respuesta_examen_valoracion.TipoEvaluador;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespuestaExamenValoracionResponseDto {
    
    private Long id;
    private String linkFormatoB;
    private String linkFormatoC;
    private String linkObservaciones;
    private List<AnexoRespuestaExamenValoracionDto> anexos;
    private ConceptosVarios respuestaExamenValoracion;
    private LocalDate fechaMaximaEntrega;
    private Long idEvaluador;
    private TipoEvaluador tipoEvaluador;
    
}
