package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.GeneracionResolucionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.comite.GeneracionResolucionComiteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.GeneracionResolucionCoordinadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteDto;

@Mapper(componentModel = "spring")
public interface GeneracionResolucionMapper extends GenericMapper<GeneracionResolucionDto, GeneracionResolucion> {

    // Métodos de mapeo para el DTO de Coordinador
    GeneracionResolucion toEntity(GeneracionResolucionCoordinadorDto docenteDto);

    GeneracionResolucionCoordinadorDto toCoordinadorDto(GeneracionResolucion entity);

    // Métodos de mapeo para el DTO de Comite
    GeneracionResolucion toEntity(GeneracionResolucionComiteDto coordinadorDto);

    GeneracionResolucionComiteDto toComiteDto(GeneracionResolucion entity);
}
