package com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.DirectorAndCodirectorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.ObtenerDocumentosParaEnvioDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;

public interface GeneracionResolucionService {

        public List<DirectorAndCodirectorResponseDto> listarDirectorAndCodirector();

        public GeneracionResolucionDocenteResponseDto insertarInformacionDocente(
                        Long idTrabajoGrado,
                        GeneracionResolucionDocenteDto generacionResolucion,
                        BindingResult result);

        public GeneracionResolucionCoordinadorFase1ResponseDto insertarInformacionCoordinadorFase1(
                        Long idTrabajoGrado,
                        GeneracionResolucionCoordinadorFase1Dto generacionResolucionDto,
                        BindingResult result);

        public GeneracionResolucionCoordinadorFase2ResponseDto insertarInformacionCoordinadorFase2(
                        Long idTrabajoGrado,
                        GeneracionResolucionCoordinadorFase2Dto generacionResolucion,
                        BindingResult result);

        public GeneracionResolucionCoordinadorFase3ResponseDto insertarInformacionCoordinadorFase3(
                        Long idTrabajoGrado,
                        GeneracionResolucionCoordinadorFase3Dto generacionResolucion,
                        BindingResult result);

        public GeneracionResolucionDocenteResponseDto listarInformacionDocente(Long idTrabajoGrado);

        public GeneracionResolucionCoordinadorFase1ResponseDto listarInformacionCoordinadorFase1(Long idTrabajoGrado);

        public GeneracionResolucionCoordinadorFase2ResponseDto listarInformacionCoordinadorFase2(Long idTrabajoGrado);

        public GeneracionResolucionCoordinadorFase3ResponseDto listarInformacionCoordinadorFase3(Long idTrabajoGrado);

        public String descargarArchivo(RutaArchivoDto rutaArchivo);

        public ObtenerDocumentosParaEnvioDto obtenerDocumentosParaEnviarAlComite(Long idTrabajoGrado);

        public GeneracionResolucionDocenteResponseDto actualizarInformacionDocente(
                        Long idTrabajoGrado,
                        GeneracionResolucionDocenteDto generacionResolucionDocenteDto,
                        BindingResult result);

        public GeneracionResolucionCoordinadorFase1ResponseDto actualizarInformacionCoordinadorFase1(
                        Long idTrabajoGrado,
                        GeneracionResolucionCoordinadorFase1Dto generacionResolucionDocenteDto,
                        BindingResult result);

        public GeneracionResolucionCoordinadorFase2ResponseDto actualizarInformacionCoordinadorFase2(
                        Long idTrabajoGrado,
                        GeneracionResolucionCoordinadorFase2Dto generacionResolucionCoordinadorFase1Dto,
                        BindingResult result);

        public GeneracionResolucionCoordinadorFase3ResponseDto actualizarInformacionCoordinadorFase3(
                        Long idTrabajoGrado,
                        GeneracionResolucionCoordinadorFase3Dto generacionResolucionDocenteDto,
                        BindingResult result);
}
