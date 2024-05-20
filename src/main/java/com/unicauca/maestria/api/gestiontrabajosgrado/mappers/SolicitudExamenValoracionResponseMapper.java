package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseDto;

@Mapper(componentModel = "spring")
public interface SolicitudExamenValoracionResponseMapper extends GenericMapper<SolicitudExamenValoracionResponseDto, SolicitudExamenValoracion> {

    // Métodos de mapeo para el DTO de Docente
    //SolicitudExamenValoracion toEntity(SolicitudExamenValoracionDocenteDto docenteDto);

    SolicitudExamenValoracionDocenteResponseDto toDocenteDto(SolicitudExamenValoracion entity);

    SolicitudExamenValoracionDocenteResponseDto toDocenteResponseDto(SolicitudExamenValoracionResponseDto entity);

    // Métodos de mapeo para el DTO de Coordinador
    //SolicitudExamenValoracion toEntity(SolicitudExamenValoracionCoordinadorDto coordinadorDto);

    SolicitudExamenValoracionCoordinadorResponseDto toCoordinadorDto(SolicitudExamenValoracion entity);
    
    // Agrega este método
    SolicitudExamenValoracionResponseDto toDto(SolicitudExamenValoracionResponseDto entity);
}

