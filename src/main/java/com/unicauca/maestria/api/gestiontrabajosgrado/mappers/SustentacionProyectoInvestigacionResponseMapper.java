package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_proyecto_investigacion.SustentacionProyectoInvestigacion;
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
                extends GenericMapper<SustentacionTrabajoInvestigacionDto, SustentacionProyectoInvestigacion> {

        SustentacionProyectoInvestigacion toEntity(SustentacionTrabajoInvestigacionDocenteDto dto);

        SustentacionTrabajoInvestigacionDocenteResponseDto toDocenteDto(SustentacionProyectoInvestigacion entity);

        SustentacionProyectoInvestigacion toEntity(SustentacionTrabajoInvestigacionCoordinadorFase1Dto dto);

        STICoordinadorFase1ResponseDto toCoordinadorFase1Dto(
                        SustentacionProyectoInvestigacion entity);

        SustentacionProyectoInvestigacion toEntity(SustentacionTrabajoInvestigacionCoordinadorFase2Dto dto);

        STICoordinadorFase2ResponseDto toCoordinadorFase2Dto(
                        SustentacionProyectoInvestigacion entity);

        SustentacionProyectoInvestigacion toEntity(SustentacionTrabajoInvestigacionEstudianteDto dto);

        SustentacionTrabajoInvestigacionEstudianteResponseDto toEstudianteDto(SustentacionProyectoInvestigacion entity);

        SustentacionProyectoInvestigacion toEntity(SustentacionTrabajoInvestigacionCoordinadorFase3Dto dto);

        STICoordinadorFase3ResponseDto toCoordinadorFase3Dto(
                        SustentacionProyectoInvestigacion entity);

        SustentacionProyectoInvestigacion toEntity(SustentacionTrabajoInvestigacionCoordinadorFase4Dto dto);

        STICoordinadorFase4ResponseDto toCoordinadorFase4Dto(
                        SustentacionProyectoInvestigacion entity);
}
