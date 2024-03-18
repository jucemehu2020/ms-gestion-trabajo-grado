package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.ExamenValoracionDto;

@Mapper(componentModel = "spring")
public interface ExamenValoracionMapper extends GenericMapper<ExamenValoracionDto, SolicitudExamenValoracion> {
    // Métodos de mapeo
}
