package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.GeneracionResolucionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteResponseDto;

@Mapper(componentModel = "spring")
public interface GeneracionResolucionResponseMapper
        extends GenericMapper<GeneracionResolucionResponseDto, GeneracionResolucion> {

    // Docente
    GeneracionResolucionDocenteResponseDto toDocenteResponseDto(GeneracionResolucionResponseDto dto);

    GeneracionResolucionDocenteResponseDto toDocenteDto(GeneracionResolucion entity);

    // Coordinador - Fase 1
    GeneracionResolucionDocenteResponseDto toCoordinadorFase1ResponseDto(GeneracionResolucionResponseDto dto);

    GeneracionResolucionCoordinadorFase1ResponseDto toCoordinadorFase1Dto(GeneracionResolucion entity);

    // Coordinador - Fase 2
    GeneracionResolucionDocenteResponseDto toCoordinadorFase2ResponseDto(GeneracionResolucionResponseDto dto);

    GeneracionResolucionCoordinadorFase2ResponseDto toCoordinadorFase2Dto(GeneracionResolucion entity);

}
