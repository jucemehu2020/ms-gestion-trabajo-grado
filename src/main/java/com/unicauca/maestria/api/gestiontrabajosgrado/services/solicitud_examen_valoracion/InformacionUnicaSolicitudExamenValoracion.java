package com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion;

import java.util.function.Function;
import org.springframework.stereotype.Service;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.CamposUnicosSolicitudExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteDto;

@Service
public class InformacionUnicaSolicitudExamenValoracion implements Function<SolicitudExamenValoracionDocenteDto, CamposUnicosSolicitudExamenValoracionDto>{

    @Override
	public CamposUnicosSolicitudExamenValoracionDto apply(SolicitudExamenValoracionDocenteDto estudianteSaveDto) {

		return CamposUnicosSolicitudExamenValoracionDto.builder()
				.idTrabajoGrados(estudianteSaveDto.getIdTrabajoGrados())
				.build();
	}
}
