package com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.experto.ExpertoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.DocenteInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.ExpertoInfoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.SolicitudExamenValoracionResponseDto;

public interface SolicitudExamenValoracionService {

    public List<DocenteInfoDto> listarDocentes();

    public List<ExpertoInfoDto> listarExpertos();

    public SolicitudExamenValoracionResponseDto crear(SolicitudExamenValoracionDto oficio, BindingResult result);

    public SolicitudExamenValoracionDto buscarPorId(Long idTrabajoGrado);

    public SolicitudExamenValoracionResponseDto actualizar(Long id, SolicitudExamenValoracionDto examenValoracionDto, BindingResult result);

    public String descargarArchivo(RutaArchivoDto rutaArchivo);
}
