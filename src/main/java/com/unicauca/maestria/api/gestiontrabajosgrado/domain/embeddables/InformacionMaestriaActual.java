package com.unicauca.maestria.api.gestiontrabajosgrado.domain.embeddables;


import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.EstadoMaestriaActual;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.ModalidadIngreso;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.enums.estudiante.ModalidadMaestriaActual;
import com.unicauca.maestria.api.gestiontrabajosgrado.common.util.BooleanConverter;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
@Embeddable
public class InformacionMaestriaActual {

	@Enumerated(EnumType.STRING)
	private EstadoMaestriaActual estadoMaestria;
	
	@Enumerated(EnumType.STRING)
	private ModalidadMaestriaActual modalidad;
	
	@Convert(converter = BooleanConverter.class)
	private Boolean esEstudianteDoctorado;
	
	private String tituloDoctorado;
	private Integer Cohorte;
	private String periodoIngreso;
	
	@Enumerated(EnumType.STRING)
	private ModalidadIngreso modalidadIngreso;
	
	private Integer semestreAcademico;

	private Integer semestreFinanciero;
	
}
