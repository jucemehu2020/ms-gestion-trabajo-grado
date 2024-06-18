package com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.DirectorAndCodirectorResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.EnvioEmailCorrecionesDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.GeneracionResolucionCoordinadorFase1ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.InformacionEnvioComiteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_1.ObtenerDocumentosParaEnvioDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_2.GeneracionResolucionCoordinadorFase2ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3Dto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.coordinador.fase_3.GeneracionResolucionCoordinadorFase3ResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.docente.GeneracionResolucionDocenteResponseDto;

public interface GeneracionResolucionService {

        public List<DirectorAndCodirectorResponseDto> listarDirectorAndCodirector();

        // public GeneracionResolucionDto crear(GeneracionResolucionDto
        // generacionResolucion,
        // BindingResult result);

        public GeneracionResolucionDocenteResponseDto insertarInformacionDocente(
                        GeneracionResolucionDocenteDto generacionResolucion,
                        BindingResult result);

        public GeneracionResolucionCoordinadorFase2ResponseDto insertarInformacionCoordinadorFase2(
                        GeneracionResolucionCoordinadorFase2Dto generacionResolucion,
                        BindingResult result);

        public GeneracionResolucionCoordinadorFase3ResponseDto insertarInformacionCoordinadorFase3(
                        GeneracionResolucionCoordinadorFase3Dto generacionResolucion,
                        BindingResult result);

        // public GeneracionResolucionDto buscarPorId(Long idTrabajoGrado);

        public GeneracionResolucionDocenteResponseDto listarInformacionDocente(Long idTrabajoGrado);

        public GeneracionResolucionCoordinadorFase2ResponseDto listarInformacionCoordinadorFase2(Long idTrabajoGrado);

        public GeneracionResolucionCoordinadorFase3ResponseDto listarInformacionCoordinadorFase3(Long idTrabajoGrado);

        // public GeneracionResolucionDto actualizar(Long id, GeneracionResolucionDto
        // generacionResolucion,
        // BindingResult result);

        public String descargarArchivo(RutaArchivoDto rutaArchivo);

        public GeneracionResolucionCoordinadorFase1ResponseDto insertarInformacionCoordinadorFase1(
                        GeneracionResolucionCoordinadorFase1Dto generacionResolucionDto,
                        BindingResult result);

        public ObtenerDocumentosParaEnvioDto obtenerDocumentosParaEnviarAlComite(Long idGeneracionResolucion);

        public InformacionEnvioComiteDto enviarCorreoComite(InformacionEnvioComiteDto informacionEnvioComiteDto);

        public EnvioEmailCorrecionesDto enviarCorreoCorrecion(EnvioEmailCorrecionesDto envioEmailCorrecionesDto);
}
