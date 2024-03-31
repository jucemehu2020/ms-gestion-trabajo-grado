package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionResponseDto;

@Mapper(componentModel = "spring")
public interface SolicitudExamenValoracionResponseMapper
        extends GenericMapper<SolicitudExamenValoracionResponseDto, SolicitudExamenValoracion> {
}
