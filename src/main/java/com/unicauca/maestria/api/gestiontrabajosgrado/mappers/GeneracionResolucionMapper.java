package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.GeneracionResolucionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteDto;

@Mapper(componentModel = "spring")
public interface GeneracionResolucionMapper extends GenericMapper<GeneracionResolucionDto, GeneracionResolucion> {

    // Métodos de mapeo para el DTO de Coordinador
    GeneracionResolucion toEntity(GeneracionResolucionDocenteDto docenteDto);

    // GeneracionResolucionDocenteDto toDocenteDto(GeneracionResolucion entity);

    // Métodos de mapeo para el DTO de Comite
    GeneracionResolucion toEntity(GeneracionResolucionCoordinadorFase1Dto coordinadorDto);

    // GeneracionResolucionCoordinadorFase1Dto
    // toCoordinadorFase1Dto(GeneracionResolucion entity);

    // Métodos de mapeo para el DTO de Comite
    GeneracionResolucion toEntity(GeneracionResolucionCoordinadorFase2Dto coordinadorDto);

    // GeneracionResolucionCoordinadorFase2Dto
    // toCoordinadorFase2Dto(GeneracionResolucion entity);
}
