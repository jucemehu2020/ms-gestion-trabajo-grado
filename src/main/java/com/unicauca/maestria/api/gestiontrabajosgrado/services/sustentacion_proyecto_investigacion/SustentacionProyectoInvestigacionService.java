package com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion;

import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto;

public interface SustentacionProyectoInvestigacionService {
    
    public SustentacionTrabajoInvestigacionDto crear(SustentacionTrabajoInvestigacionDto oficio, BindingResult result);

    public SustentacionTrabajoInvestigacionDto buscarPorId(Long idTrabajoGrado);

    public SustentacionTrabajoInvestigacionDto actualizar(Long id, SustentacionTrabajoInvestigacionDto examenValoracionDto, BindingResult result);
}
