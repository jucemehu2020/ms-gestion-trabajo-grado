package com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente;

import java.util.ArrayList;
import java.util.List;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.docente.EscalafonDocente;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.docente.EstadoPersona;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.docente.TipoVinculacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.common.PersonaDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor @Builder
public class DocenteResponseDto {
	
	private Long id;
	private EstadoPersona estado;
	private PersonaDto persona;
	private String codigo;	
	private String facultad;
	private String departamento;
	private EscalafonDocente escalafon;
	private String observacion;
	private List<LineaInvestigacionDto> lineasInvestigacion;
	private TipoVinculacion tipoVinculacion;
	private List<TituloDto> titulos;
	
	public DocenteResponseDto() {
		lineasInvestigacion = new ArrayList<>();
		titulos = new ArrayList<>();
	}
}
