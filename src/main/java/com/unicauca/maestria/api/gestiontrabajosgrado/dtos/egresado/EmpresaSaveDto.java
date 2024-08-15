package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.egresado;

import lombok.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaSaveDto {
    private Long id;

    @NotNull
    private Long idEstudiante;

    @NotBlank
    private String nombre;

    @NotBlank
    private String ubicacion;

    @NotBlank
    private String cargo;

    @NotBlank
    private String jefeDirecto;

    @NotBlank
    private String telefono;

    @NotBlank
    private String correo;

    @NotBlank
    private String estado;
}
