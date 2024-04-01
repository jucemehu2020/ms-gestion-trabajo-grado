package com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion;

import java.util.function.Function;
import org.springframework.stereotype.Service;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.CamposUnicosGenerarResolucionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.GeneracionResolucionDto;

@Service
public class InformacionUnicaGeneracionResolucion implements Function<GeneracionResolucionDto, CamposUnicosGenerarResolucionDto>{

    @Override
	public CamposUnicosGenerarResolucionDto apply(GeneracionResolucionDto generarResolucion) {

		return CamposUnicosGenerarResolucionDto.builder()
				.idTrabajoGrados(generarResolucion.getIdTrabajoGrados())
				.build();
	}
}
