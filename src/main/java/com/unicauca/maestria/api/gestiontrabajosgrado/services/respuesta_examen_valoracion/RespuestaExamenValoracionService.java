package com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion;

import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.Cancelado.ExamenValoracionCanceladoDto;

public interface RespuestaExamenValoracionService {

        public RespuestaExamenValoracionResponseDto insertarInformacion(Long idTrabajoGrado,
                        RespuestaExamenValoracionDto respuestaExamenValoracion,
                        BindingResult result);

        public ExamenValoracionCanceladoDto insertarInformacionCancelado(
                        Long idTrabajoGrado,
                        ExamenValoracionCanceladoDto examenValoracionCanceladoDto,
                        BindingResult result);

        public Map<String, List<RespuestaExamenValoracionResponseDto>> buscarPorId(Long idTrabajoGrado);

        public RespuestaExamenValoracionResponseDto actualizar(Long idRespuestaExamen,
                        RespuestaExamenValoracionDto respuestaExamenValoracionDto,
                        BindingResult result);

        public Boolean evaluadorNoRespondio(Long idTrabajoGrado);

}
