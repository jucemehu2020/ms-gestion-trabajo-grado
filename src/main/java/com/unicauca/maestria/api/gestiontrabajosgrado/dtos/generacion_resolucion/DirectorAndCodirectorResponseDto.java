package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.TipoIdentificacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectorAndCodirectorResponseDto {
    private Long id;
    private TipoIdentificacion tipoIdentificacion;
    private Long identificacion;
    private String nombre;
    private String apellido;

}
