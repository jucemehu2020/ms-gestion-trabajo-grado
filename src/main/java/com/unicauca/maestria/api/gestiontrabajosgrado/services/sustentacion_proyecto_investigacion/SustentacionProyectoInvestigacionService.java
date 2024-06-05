package com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.STICoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_1.SustentacionTrabajoInvestigacionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.STICoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_2.SustentacionTrabajoInvestigacionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.STICoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.coordinador.fase_3.SustentacionTrabajoInvestigacionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.docente.SustentacionTrabajoInvestigacionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante.SustentacionTrabajoInvestigacionEstudianteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.estudiante.SustentacionTrabajoInvestigacionEstudianteResponseDto;

public interface SustentacionProyectoInvestigacionService {

        // public SustentacionTrabajoInvestigacionDto
        // crear(SustentacionTrabajoInvestigacionDto oficio,
        // BindingResult result);

        public SustentacionTrabajoInvestigacionDocenteResponseDto insertarInformacionDocente(
                        SustentacionTrabajoInvestigacionDocenteDto sustentacionDto,
                        BindingResult result);

        public STICoordinadorFase1ResponseDto insertarInformacionCoordinadoFase1(
                        SustentacionTrabajoInvestigacionCoordinadorFase1Dto sustentacionDto,
                        BindingResult result);

        public STICoordinadorFase2ResponseDto insertarInformacionCoordinadoFase2(
                        SustentacionTrabajoInvestigacionCoordinadorFase2Dto sustentacionDto,
                        BindingResult result);

        public SustentacionTrabajoInvestigacionEstudianteResponseDto insertarInformacionEstudiante(
                        SustentacionTrabajoInvestigacionEstudianteDto sustentacionDto,
                        BindingResult result);

        public STICoordinadorFase3ResponseDto insertarInformacionCoordinadoFase3(
                        SustentacionTrabajoInvestigacionCoordinadorFase3Dto sustentacionDto,
                        BindingResult result);

        // public SustentacionTrabajoInvestigacionDto buscarPorId(Long idTrabajoGrado);

        public SustentacionTrabajoInvestigacionDocenteResponseDto listarInformacionDocente(Long idTrabajoGrado);

        public STICoordinadorFase1ResponseDto listarInformacionCoordinadorFase1(
                        Long idTrabajoGrado);

        public STICoordinadorFase2ResponseDto listarInformacionCoordinadorFase2(
                        Long idTrabajoGrado);

        public SustentacionTrabajoInvestigacionEstudianteResponseDto listarInformacionEstudiante(Long idTrabajoGrado);

        public STICoordinadorFase3ResponseDto listarInformacionCoordinadorFase3(
                        Long idTrabajoGrado);

        // public SustentacionTrabajoInvestigacionDto listarInformacionCoordinador(Long idTrabajoGrado);

        // public SustentacionTrabajoInvestigacionDto actualizar(Long id,
        //                 SustentacionTrabajoInvestigacionDto examenValoracionDto, BindingResult result);

        public String descargarArchivo(RutaArchivoDto rutaArchivo);
}
