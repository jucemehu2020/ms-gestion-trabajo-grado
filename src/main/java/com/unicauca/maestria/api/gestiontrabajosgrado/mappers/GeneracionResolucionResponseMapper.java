package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.generacion_resolucion.GeneracionResolucion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.solicitud_examen_valoracion.SolicitudExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.GeneracionResolucionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.comite.GeneracionResolucionComiteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.comite.GeneracionResolucionComiteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.GeneracionResolucionCoordinadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.GeneracionResolucionCoordinadorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.coordinador.SolicitudExamenValoracionCoordinadorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.docente.SolicitudExamenValoracionDocenteResponseDto;

@Mapper(componentModel = "spring")
public interface GeneracionResolucionResponseMapper
        extends GenericMapper<GeneracionResolucionDto, GeneracionResolucion> {

    GeneracionResolucionCoordinadorResponseDto toCoordinadorDto(GeneracionResolucion entity);

    GeneracionResolucionCoordinadorResponseDto toCoordinadorResponseDto(GeneracionResolucionDto entity);

    GeneracionResolucionComiteResponseDto toComiteDto(GeneracionResolucion entity);

    GeneracionResolucionComiteResponseDto toDto(GeneracionResolucionDto entity);
}
