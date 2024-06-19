package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.egresado;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursoSaveDto {
    private Long id;

    @NotNull
    private Long idEstudiante;

    @NotBlank
    private String nombre;

    @NotBlank
    private String orientadoA;

    @NotNull
    private LocalDate fechaInicio;

    @NotNull
    private LocalDate fechaFin;

}
