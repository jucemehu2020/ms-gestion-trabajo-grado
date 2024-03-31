package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.rta_examen_valoracion.RespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.rta_examen_valoracion.RespuestaExamenValoracion.RespuestaExamenValoracionBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto.RespuestaExamenValoracionDtoBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-31T17:11:00-0500",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240206-1609, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class RespuestaExamenValoracionMapperImpl implements RespuestaExamenValoracionMapper {

    @Override
    public RespuestaExamenValoracion toEntity(RespuestaExamenValoracionDto dto) {
        if ( dto == null ) {
            return null;
        }

        RespuestaExamenValoracionBuilder respuestaExamenValoracion = RespuestaExamenValoracion.builder();

        respuestaExamenValoracion.estadoFinalizado( dto.getEstadoFinalizado() );
        respuestaExamenValoracion.fechaMaxmiaEntrega( dto.getFechaMaxmiaEntrega() );
        respuestaExamenValoracion.linkFormatoB( dto.getLinkFormatoB() );
        respuestaExamenValoracion.linkFormatoC( dto.getLinkFormatoC() );
        respuestaExamenValoracion.observacion( dto.getObservacion() );
        respuestaExamenValoracion.observaciones( dto.getObservaciones() );
        respuestaExamenValoracion.respuestaExamenValoracion( dto.getRespuestaExamenValoracion() );

        return respuestaExamenValoracion.build();
    }

    @Override
    public RespuestaExamenValoracionDto toDto(RespuestaExamenValoracion entity) {
        if ( entity == null ) {
            return null;
        }

        RespuestaExamenValoracionDtoBuilder respuestaExamenValoracionDto = RespuestaExamenValoracionDto.builder();

        respuestaExamenValoracionDto.estadoFinalizado( entity.getEstadoFinalizado() );
        respuestaExamenValoracionDto.fechaMaxmiaEntrega( entity.getFechaMaxmiaEntrega() );
        respuestaExamenValoracionDto.linkFormatoB( entity.getLinkFormatoB() );
        respuestaExamenValoracionDto.linkFormatoC( entity.getLinkFormatoC() );
        respuestaExamenValoracionDto.observacion( entity.getObservacion() );
        respuestaExamenValoracionDto.observaciones( entity.getObservaciones() );
        respuestaExamenValoracionDto.respuestaExamenValoracion( entity.getRespuestaExamenValoracion() );

        return respuestaExamenValoracionDto.build();
    }

    @Override
    public List<RespuestaExamenValoracion> toEntityList(List<RespuestaExamenValoracionDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<RespuestaExamenValoracion> list = new ArrayList<RespuestaExamenValoracion>( dtos.size() );
        for ( RespuestaExamenValoracionDto respuestaExamenValoracionDto : dtos ) {
            list.add( toEntity( respuestaExamenValoracionDto ) );
        }

        return list;
    }

    @Override
    public List<RespuestaExamenValoracionDto> toDtoList(List<RespuestaExamenValoracion> entities) {
        if ( entities == null ) {
            return null;
        }

        List<RespuestaExamenValoracionDto> list = new ArrayList<RespuestaExamenValoracionDto>( entities.size() );
        for ( RespuestaExamenValoracion respuestaExamenValoracion : entities ) {
            list.add( toDto( respuestaExamenValoracion ) );
        }

        return list;
    }
}
