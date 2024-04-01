package com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion;

import java.util.function.Function;
import org.springframework.stereotype.Service;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.CamposUnicosRespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;

@Service
public class InformacionUnicaRespuestaExamenValoracion implements Function<RespuestaExamenValoracionDto, CamposUnicosRespuestaExamenValoracionDto>{

    @Override
	public CamposUnicosRespuestaExamenValoracionDto apply(RespuestaExamenValoracionDto respuestaExamenValoracion) {

		return CamposUnicosRespuestaExamenValoracionDto.builder()
				.idTrabajoGrados(respuestaExamenValoracion.getIdTrabajoGrados())
				.build();
	}
}
