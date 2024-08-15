package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.GeneracionResolucionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteDto;

@Mapper(componentModel = "spring")
public interface GeneracionResolucionMapper extends GenericMapper<GeneracionResolucionDto, GeneracionResolucion> {

    GeneracionResolucion toEntity(GeneracionResolucionDocenteDto docenteDto);

    GeneracionResolucion toEntity(GeneracionResolucionCoordinadorFase2Dto coordinadorDto);

    GeneracionResolucion toEntity(GeneracionResolucionCoordinadorFase3Dto coordinadorDto);

}
