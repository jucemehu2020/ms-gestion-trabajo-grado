package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformacionTrabajoGradoResponseDto {
    private Long id;
    private Long identificacion;
    private String nombreCompleto;
    private String correoElectronico;
    private Integer numeroEstado;
    private String estado;

}
