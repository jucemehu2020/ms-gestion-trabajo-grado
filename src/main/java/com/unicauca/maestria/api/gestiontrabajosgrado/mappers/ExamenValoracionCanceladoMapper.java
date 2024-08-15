package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.respuesta_examen_valoracion.ExamenValoracionCancelado;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.Cancelado.ExamenValoracionCanceladoDto;

@Mapper(componentModel = "spring")
public interface ExamenValoracionCanceladoMapper
                extends GenericMapper<ExamenValoracionCanceladoDto, ExamenValoracionCancelado> {

}
