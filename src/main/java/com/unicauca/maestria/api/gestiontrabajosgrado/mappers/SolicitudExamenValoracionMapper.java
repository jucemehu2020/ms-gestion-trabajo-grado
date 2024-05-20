package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteDto;

@Mapper(componentModel = "spring")
public interface SolicitudExamenValoracionMapper extends GenericMapper<SolicitudExamenValoracionDto, SolicitudExamenValoracion> {

    // Métodos de mapeo para el DTO de Docente
    SolicitudExamenValoracion toEntity(SolicitudExamenValoracionDocenteDto docenteDto);
    SolicitudExamenValoracionDocenteDto toDocenteDto(SolicitudExamenValoracion entity);

    // Métodos de mapeo para el DTO de Coordinador
    SolicitudExamenValoracion toEntity(SolicitudExamenValoracionCoordinadorDto coordinadorDto);
    SolicitudExamenValoracionCoordinadorDto toCoordinadorDto(SolicitudExamenValoracion entity);
}
