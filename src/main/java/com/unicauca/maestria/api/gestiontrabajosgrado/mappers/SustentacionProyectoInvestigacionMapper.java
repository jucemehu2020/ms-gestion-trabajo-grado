package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.sustentacion_trabajo_investigacion.SustentacionTrabajoInvestigacion;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.sustentacion_proyecto_investigacion.SustentacionTrabajoInvestigacionDto;

@Mapper(componentModel = "spring")
public interface SustentacionProyectoInvestigacionMapper extends GenericMapper<SustentacionTrabajoInvestigacionDto, SustentacionTrabajoInvestigacion>{
    
}
