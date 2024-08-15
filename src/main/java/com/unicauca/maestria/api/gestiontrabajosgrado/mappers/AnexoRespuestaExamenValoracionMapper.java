package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.AnexoRespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.AnexoRespuestaExamenValoracionDto;

@Mapper(componentModel = "spring")
public interface AnexoRespuestaExamenValoracionMapper
        extends GenericMapper<AnexoRespuestaExamenValoracionDto, AnexoRespuestaExamenValoracion> {

}