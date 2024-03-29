package com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion;

import java.util.function.Function;
import org.springframework.stereotype.Service;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.CamposUnicosSolicitudExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.ExamenValoracionDto;

@Service
public class InformacionUnicaSolicitudExamenValoracion implements Function<ExamenValoracionDto, CamposUnicosSolicitudExamenValoracionDto>{

    @Override
	public CamposUnicosSolicitudExamenValoracionDto apply(ExamenValoracionDto estudianteSaveDto) {

		return CamposUnicosSolicitudExamenValoracionDto.builder()
				.idTrabajoGrados(estudianteSaveDto.getIdTrabajoGrados())
				.build();
	}
}
