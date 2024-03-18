package com.unicauca.maestria.api.gestiontrabajosgrado.services.solicitud_examen_valoracion;

import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.solicitud_examen_valoracion.ExamenValoracionDto;

public interface SolicitudExamenValoracionService {

    public DocenteResponseDto listarDocentes();

    public ExamenValoracionDto crear(ExamenValoracionDto oficio, BindingResult result);

    public ExamenValoracionDto buscarPorId(Long idTrabajoGrado);

    public ExamenValoracionDto actualizar(Long id, ExamenValoracionDto examenValoracionDto, BindingResult result);
}
