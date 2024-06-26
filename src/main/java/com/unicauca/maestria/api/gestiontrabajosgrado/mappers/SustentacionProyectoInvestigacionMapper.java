package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.SustentacionTrabajoInvestigacionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.SustentacionTrabajoInvestigacionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.SustentacionTrabajoInvestigacionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4.SustentacionTrabajoInvestigacionCoordinadorFase4Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante.SustentacionTrabajoInvestigacionEstudianteDto;

@Mapper(componentModel = "spring")
public interface SustentacionProyectoInvestigacionMapper
        extends GenericMapper<SustentacionTrabajoInvestigacionDto, SustentacionTrabajoInvestigacion> {

    // Docente
    SustentacionTrabajoInvestigacion toEntity(SustentacionTrabajoInvestigacionDocenteDto docenteDto);

    SustentacionTrabajoInvestigacionDocenteDto toDocenteDto(SustentacionTrabajoInvestigacion entity);

    // Coordinador Fase 1
    SustentacionTrabajoInvestigacion toEntity(SustentacionTrabajoInvestigacionCoordinadorFase1Dto comiteDto);

    SustentacionTrabajoInvestigacionCoordinadorFase1Dto toCoordinaforFase1Dto(SustentacionTrabajoInvestigacion entity);

    // Coordinador Fase 2
    SustentacionTrabajoInvestigacion toEntity(SustentacionTrabajoInvestigacionCoordinadorFase2Dto comiteDto);

    SustentacionTrabajoInvestigacionCoordinadorFase2Dto toCoordinaforFase2Dto(SustentacionTrabajoInvestigacion entity);

    // Coordinador Fase 3
    SustentacionTrabajoInvestigacion toEntity(SustentacionTrabajoInvestigacionCoordinadorFase3Dto comiteDto);

    SustentacionTrabajoInvestigacionCoordinadorFase3Dto toCoordinaforFase3Dto(SustentacionTrabajoInvestigacion entity);

    // Estudiante
    SustentacionTrabajoInvestigacion toEntity(SustentacionTrabajoInvestigacionEstudianteDto comiteDto);

    SustentacionTrabajoInvestigacionEstudianteDto toEstudianteDto(SustentacionTrabajoInvestigacion entity);

    // Coordinador Fase 3
    SustentacionTrabajoInvestigacion toEntity(SustentacionTrabajoInvestigacionCoordinadorFase4Dto comiteDto);

    SustentacionTrabajoInvestigacionCoordinadorFase4Dto toCoordinaforFase4Dto(SustentacionTrabajoInvestigacion entity);

    SolicitudExamenValoracionResponseDto toDto(SolicitudExamenValoracionResponseDto entity);
}
