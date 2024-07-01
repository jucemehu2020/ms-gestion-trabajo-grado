package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase1.SolicitudExamenValoracionResponseFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.Fase2.SolicitudExamenValoracionCoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseDto;

@Mapper(componentModel = "spring")
public interface SolicitudExamenValoracionResponseMapper extends GenericMapper<SolicitudExamenValoracionResponseDto, SolicitudExamenValoracion> {

    
    // Métodos de mapeo para el DTO de Docente
    //SolicitudExamenValoracion toEntity(SolicitudExamenValoracionDocenteDto docenteDto);

    SolicitudExamenValoracionDocenteResponseDto toDocenteDto(SolicitudExamenValoracion entity);

    SolicitudExamenValoracionDocenteResponseDto toDocenteResponseDto(SolicitudExamenValoracionResponseDto entity);

    // Métodos de mapeo para el DTO de Coordinador

    SolicitudExamenValoracionResponseFase1Dto toCoordinadorFase1Dto(SolicitudExamenValoracion entity);

    //SolicitudExamenValoracion toEntity(SolicitudExamenValoracionCoordinadorDto coordinadorDto);

    SolicitudExamenValoracionCoordinadorFase2ResponseDto toCoordinadorFase2Dto(SolicitudExamenValoracion entity);
    
    // Agrega este método
    SolicitudExamenValoracionResponseDto toDto(SolicitudExamenValoracionResponseDto entity);
}

