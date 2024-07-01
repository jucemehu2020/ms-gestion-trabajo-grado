package com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion;

import org.springframework.validation.BindingResult;

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

public interface SustentacionProyectoInvestigacionService {

        public SustentacionTrabajoInvestigacionDocenteResponseDto insertarInformacionDocente(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionDocenteDto sustentacionDto,
                        BindingResult result);

        public STICoordinadorFase1ResponseDto insertarInformacionCoordinadoFase1(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionDto,
                        BindingResult result);

        public STICoordinadorFase2ResponseDto insertarInformacionCoordinadoFase2(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionDto,
                        BindingResult result);

        public STICoordinadorFase3ResponseDto insertarInformacionCoordinadoFase3(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionDto,
                        BindingResult result);

        public SustentacionTrabajoInvestigacionEstudianteResponseDto insertarInformacionEstudiante(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionEstudianteDto sustentacionDto,
                        BindingResult result);

        public STICoordinadorFase4ResponseDto insertarInformacionCoordinadoFase4(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionDto,
                        BindingResult result);

        public SustentacionTrabajoInvestigacionDocenteResponseDto listarInformacionDocente(Long idTrabajoGrado);

        public STICoordinadorFase1ResponseDto listarInformacionCoordinadorFase1(
                        Long idTrabajoGrado);

        public STICoordinadorFase2ResponseDto listarInformacionCoordinadorFase2(
                        Long idTrabajoGrado);

        public STICoordinadorFase3ResponseDto listarInformacionCoordinadorFase3(
                        Long idTrabajoGrado);

        public SustentacionTrabajoInvestigacionEstudianteResponseDto listarInformacionEstudiante(Long idTrabajoGrado);

        public STICoordinadorFase4ResponseDto listarInformacionCoordinadorFase4(
                        Long idTrabajoGrado);

        public Boolean verificarEgresado(Long idTrabajoGrado);

        public SustentacionTrabajoInvestigacionDocenteResponseDto actualizarInformacionDocente(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionDocenteDto sustentacionDto,
                        BindingResult result);

        public STICoordinadorFase1ResponseDto actualizarInformacionCoordinadoFase1(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionDto,
                        BindingResult result);

        public STICoordinadorFase2ResponseDto actualizarInformacionCoordinadoFase2(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionDto,
                        BindingResult result);

        public STICoordinadorFase3ResponseDto actualizarInformacionCoordinadoFase3(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionDto,
                        BindingResult result);

        public SustentacionTrabajoInvestigacionEstudianteResponseDto actualizarInformacionEstudiante(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionEstudianteDto sustentacionDto,
                        BindingResult result);

        public STICoordinadorFase4ResponseDto actualizarInformacionCoordinadoFase4(
                        Long idTrabajoGrado,
                        SustentacionTrabajoInvestigacionCoordinadorFase4Dto sustentacionDto,
                        BindingResult result);
}
