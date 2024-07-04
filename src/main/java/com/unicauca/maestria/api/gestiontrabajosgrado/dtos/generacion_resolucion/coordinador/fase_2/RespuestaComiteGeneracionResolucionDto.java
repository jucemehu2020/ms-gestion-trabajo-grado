package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2;

import java.time.LocalDate;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.generales.Concepto;

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

    private Concepto conceptoComite;
    private String numeroActa;
    private LocalDate fechaActa;
}
