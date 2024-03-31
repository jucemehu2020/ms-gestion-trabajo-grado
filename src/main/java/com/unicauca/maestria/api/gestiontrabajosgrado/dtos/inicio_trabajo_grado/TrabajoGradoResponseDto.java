package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado;

import java.time.LocalDate;

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
	private String estado;
	private LocalDate fechaCreacion;
	private Integer numeroEstado;

}
