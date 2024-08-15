package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.AnexoSolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.AnexoSolicitudExamenValoracionDto;

@Mapper(componentModel = "spring")
public interface AnexoSolicitudExamenValoracionMapper
        extends GenericMapper<AnexoSolicitudExamenValoracionDto, AnexoSolicitudExamenValoracion> {

}