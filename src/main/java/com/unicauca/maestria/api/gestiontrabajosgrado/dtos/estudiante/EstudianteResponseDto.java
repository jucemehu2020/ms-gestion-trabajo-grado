package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante;

import java.util.List;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class EstudianteResponseDto {

	private Long idEstudiante;
	private List<TrabajoGradoResponseDto> trabajoGrado;
}