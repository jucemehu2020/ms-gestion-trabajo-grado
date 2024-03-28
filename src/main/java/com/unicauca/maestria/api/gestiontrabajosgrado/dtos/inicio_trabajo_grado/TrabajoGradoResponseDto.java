package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado;

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
public class TrabajoGradoResponseDto {
	private Long id;

	@NotBlank
	private String estado;

	@NotNull
	private LocalDate fechaCreacion;

	@NotBlank
	private Integer numeroEstado;
}
