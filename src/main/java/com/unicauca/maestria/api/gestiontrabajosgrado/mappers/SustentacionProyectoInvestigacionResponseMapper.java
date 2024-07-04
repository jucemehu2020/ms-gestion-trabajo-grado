package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.STICoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.SustentacionTrabajoInvestigacionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.STICoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.SustentacionTrabajoInvestigacionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.STICoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.SustentacionTrabajoInvestigacionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4.STICoordinadorFase4ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_4.SustentacionTrabajoInvestigacionCoordinadorFase4Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante.SustentacionTrabajoInvestigacionEstudianteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante.SustentacionTrabajoInvestigacionEstudianteResponseDto;

@Mapper(componentModel = "spring")
public interface SustentacionProyectoInvestigacionResponseMapper
                extends GenericMapper<SustentacionTrabajoInvestigacionDto, SustentacionTrabajoInvestigacion> {

        // Docente
        SustentacionTrabajoInvestigacion toEntity(SustentacionTrabajoInvestigacionDocenteDto dto);

        SustentacionTrabajoInvestigacionDocenteResponseDto toDocenteDto(SustentacionTrabajoInvestigacion entity);

        // Coordinador Fase 1
        SustentacionTrabajoInvestigacion toEntity(SustentacionTrabajoInvestigacionCoordinadorFase1Dto dto);

        STICoordinadorFase1ResponseDto toCoordinadorFase1Dto(
                        SustentacionTrabajoInvestigacion entity);

        // Coordinador Fase 2
        SustentacionTrabajoInvestigacion toEntity(SustentacionTrabajoInvestigacionCoordinadorFase2Dto dto);

        STICoordinadorFase2ResponseDto toCoordinadorFase2Dto(
                        SustentacionTrabajoInvestigacion entity);

        // Estudiante
        SustentacionTrabajoInvestigacion toEntity(SustentacionTrabajoInvestigacionEstudianteDto dto);

        SustentacionTrabajoInvestigacionEstudianteResponseDto toEstudianteDto(SustentacionTrabajoInvestigacion entity);

        // Coordinador Fase 3
        SustentacionTrabajoInvestigacion toEntity(SustentacionTrabajoInvestigacionCoordinadorFase3Dto dto);

        STICoordinadorFase3ResponseDto toCoordinadorFase3Dto(
                        SustentacionTrabajoInvestigacion entity);

        // Coordinador Fase 4
        SustentacionTrabajoInvestigacion toEntity(SustentacionTrabajoInvestigacionCoordinadorFase4Dto dto);

        STICoordinadorFase4ResponseDto toCoordinadorFase4Dto(
                        SustentacionTrabajoInvestigacion entity);
}
