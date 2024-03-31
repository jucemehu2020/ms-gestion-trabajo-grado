package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante;

import java.time.LocalDate;
import java.util.List;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.embeddables.Caracterizacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.embeddables.InformacionMaestriaActual;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class EstudianteResponseDtoAll {
	private Long id;
	private String director;
	private String codirector;
	private PersonaDto persona;
	private BecaDto beca;
	private String codigo;
	private String ciudadResidencia;
	private String correoUniversidad;
	private LocalDate fechaGrado;
	private String tituloPregrado;
	private Caracterizacion caracterizacion;
	private InformacionMaestriaActual informacionMaestria;
	private List<ProrrogaDto> prorrogas;
	private List<ReingresoDto> reingresos;
	private Integer estadoTrabajoGrado;
	private List<TrabajoGradoResponseDto> trabajoGrado;
}
