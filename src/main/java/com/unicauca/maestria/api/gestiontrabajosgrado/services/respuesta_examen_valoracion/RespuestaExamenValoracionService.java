package com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion;

import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.ObtenerDocumentosParaEnvioCorreoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionInformacionGeneralDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.Fase2.ExamenValoracionCanceladoDto;

public interface RespuestaExamenValoracionService {

        public RespuestaExamenValoracionResponseDto crear(Long idTrabajoGrado,
                        RespuestaExamenValoracionDto respuestaExamenValoracion,
                        BindingResult result);

        public ExamenValoracionCanceladoDto insertarInformacionCancelado(
                        Long idTrabajoGrado,
                        ExamenValoracionCanceladoDto examenValoracionCanceladoDto,
                        BindingResult result);

        public RespuestaExamenValoracionInformacionGeneralDto listarInformacionGeneral(Long idTrabajoGrado);

        public Map<String, List<RespuestaExamenValoracionResponseDto>> buscarPorId(Long idRespuestaExamen);

        public RespuestaExamenValoracionResponseDto actualizar(Long idRespuestaExamen,
                        RespuestaExamenValoracionDto respuestaExamenValoracionDto,
                        BindingResult result);

        public String descargarArchivo(RutaArchivoDto rutaArchivo);

        public Long validarNumeroNoAprobado(Long idTrabajoGrado);

        public ObtenerDocumentosParaEnvioCorreoDto obtenerDocumentosParaEnviarCorreo(Long idRtaExamenValoracion);

}
