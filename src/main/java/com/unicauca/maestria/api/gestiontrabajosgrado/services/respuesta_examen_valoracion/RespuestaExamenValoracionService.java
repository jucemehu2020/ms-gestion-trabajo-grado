package com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion;

import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;

public interface RespuestaExamenValoracionService {
    
    public RespuestaExamenValoracionDto crear(RespuestaExamenValoracionDto respuestaExamenValoracion, BindingResult result);

    public RespuestaExamenValoracionDto buscarPorId(Long idTrabajoGrado);

    public RespuestaExamenValoracionDto actualizar(Long id, RespuestaExamenValoracionDto respuestaExamenValoracionDto, BindingResult result);
}
