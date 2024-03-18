package com.unicauca.maestria.api.gestiontrabajosgrado.services.generacion_resolucion;

import org.springframework.validation.BindingResult;

import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.generacion_resolucion.GeneracionResolucionDto;

public interface GeneracionResolucionService {

    public GeneracionResolucionDto crear(GeneracionResolucionDto generacionResolucion,
            BindingResult result);

    public GeneracionResolucionDto buscarPorId(Long idTrabajoGrado);

    public GeneracionResolucionDto actualizar(Long id, GeneracionResolucionDto generacionResolucion,
            BindingResult result);
}
