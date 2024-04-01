package com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion;

import java.util.function.Function;
import org.springframework.stereotype.Service;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.CamposUnicosSustentacionProyectoInvestigacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto;

@Service
public class InformacionUnicaSustentacionProyectoInvestigacion implements Function<SustentacionTrabajoInvestigacionDto, CamposUnicosSustentacionProyectoInvestigacionDto>{

    @Override
	public CamposUnicosSustentacionProyectoInvestigacionDto apply(SustentacionTrabajoInvestigacionDto sustentacion) {

		return CamposUnicosSustentacionProyectoInvestigacionDto.builder()
				.idTrabajoGrados(sustentacion.getIdTrabajoGrados())
				.build();
	}
}
