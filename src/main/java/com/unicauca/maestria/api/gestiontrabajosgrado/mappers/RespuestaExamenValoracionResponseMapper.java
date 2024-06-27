package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.RespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionResponseDto;

@Mapper(componentModel = "spring")
public interface RespuestaExamenValoracionResponseMapper
        extends GenericMapper<RespuestaExamenValoracionResponseDto, RespuestaExamenValoracion> {

}
