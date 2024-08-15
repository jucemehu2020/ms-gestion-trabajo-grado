package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.AnexoSustentacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.AnexoSustentacionDto;

@Mapper(componentModel = "spring")
public interface AnexoSustentacionMapper extends GenericMapper<AnexoSustentacionDto, AnexoSustentacion> {

}