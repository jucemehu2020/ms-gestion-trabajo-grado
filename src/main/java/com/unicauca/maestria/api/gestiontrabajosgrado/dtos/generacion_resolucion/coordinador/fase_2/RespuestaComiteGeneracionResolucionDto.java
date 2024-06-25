package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RespuestaComiteGeneracionResolucionDto {

    private Long idAnexoExamenValoracion;
    private Boolean conceptoComite;
    private String numeroActa;
    private String fechaActa;
}
