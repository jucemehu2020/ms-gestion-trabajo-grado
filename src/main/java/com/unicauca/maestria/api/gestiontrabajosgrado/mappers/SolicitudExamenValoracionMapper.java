package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.SolicitudExamenValoracionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteDto;

@Mapper(componentModel = "spring")
public interface SolicitudExamenValoracionMapper
        extends GenericMapper<SolicitudExamenValoracionDto, SolicitudExamenValoracion> {

    SolicitudExamenValoracion toEntity(SolicitudExamenValoracionDocenteDto docenteDto);

    SolicitudExamenValoracionDocenteDto toDocenteDto(SolicitudExamenValoracion entity);

    SolicitudExamenValoracion toEntity(SolicitudExamenValoracionCoordinadorFase2Dto coordinadorDto);

    SolicitudExamenValoracionCoordinadorFase2Dto toCoordinadorDto(SolicitudExamenValoracion entity);
}
