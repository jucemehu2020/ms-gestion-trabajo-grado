package com.unicauca.maestria.api.gestiontrabajosgrado.services.sustentacion_proyecto_investigacion;

import java.util.List;

import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.RutaArchivoDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto;

public interface SustentacionProyectoInvestigacionService {
    
    public SustentacionTrabajoInvestigacionDto crear(SustentacionTrabajoInvestigacionDto oficio, BindingResult result);

    public List<SustentacionTrabajoInvestigacionDto> buscarPorId(Long idTrabajoGrado);

    public SustentacionTrabajoInvestigacionDto actualizar(Long id, SustentacionTrabajoInvestigacionDto examenValoracionDto, BindingResult result);

    public String descargarArchivo(RutaArchivoDto rutaArchivo);
}
