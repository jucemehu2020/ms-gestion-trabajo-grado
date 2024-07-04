package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.Concepto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneracionResolucionCoordinadorFase1ResponseDto {

    private Long idGeneracionResolucion;
    private Concepto conceptoDocumentosCoordinador;

}
