package com.unicauca.maestria.api.gestiontrabajosgrado.services.respuesta_examen_valoracion;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;

public interface RespuestaExamenValoracionService {
    
    public RespuestaExamenValoracionDto crear(RespuestaExamenValoracionDto respuestaExamenValoracion, BindingResult result);

    public RespuestaExamenValoracionDto buscarPorId(Long idTrabajoGrado);

    public RespuestaExamenValoracionDto actualizar(Long id, RespuestaExamenValoracionDto respuestaExamenValoracionDto, BindingResult result);

    public String descargarArchivo(RutaArchivoDto rutaArchivo);
}
