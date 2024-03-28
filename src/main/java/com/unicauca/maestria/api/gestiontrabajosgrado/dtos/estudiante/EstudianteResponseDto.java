package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante;

import java.util.List;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class EstudianteResponseDto {

	private Long id;
	private List<TrabajoGradoDto> trabajoGrado;
}