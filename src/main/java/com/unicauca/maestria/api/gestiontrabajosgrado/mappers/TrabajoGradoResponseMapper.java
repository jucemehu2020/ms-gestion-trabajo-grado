package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;

@Mapper(componentModel = "spring")
public interface TrabajoGradoResponseMapper extends GenericMapper<TrabajoGradoResponseDto, TrabajoGrado>{
    
}
