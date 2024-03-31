package com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion;

import java.util.function.Function;
import org.springframework.stereotype.Service;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.CamposUnicosSolicitudExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionDto;

@Service
public class InformacionUnicaSolicitudExamenValoracion implements Function<SolicitudExamenValoracionDto, CamposUnicosSolicitudExamenValoracionDto>{

    @Override
	public CamposUnicosSolicitudExamenValoracionDto apply(SolicitudExamenValoracionDto estudianteSaveDto) {

		return CamposUnicosSolicitudExamenValoracionDto.builder()
				.idTrabajoGrados(estudianteSaveDto.getIdTrabajoGrados())
				.build();
	}
}
