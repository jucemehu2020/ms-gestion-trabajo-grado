package com.unicauca.maestria.api.gestiontrabajosgrado.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.TipoIdentificacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformacionEstudianteResponseDto {

    @NotNull
    private TipoIdentificacion tipoIdentificacion;

    @NotNull
    private Long identificacion;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;
}
