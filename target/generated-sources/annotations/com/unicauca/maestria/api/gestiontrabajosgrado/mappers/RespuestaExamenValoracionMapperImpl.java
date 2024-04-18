package com.unicauca.maestria.api.gestiontrabajosgrado.mappers;

import com.unicauca.maestria.api.gestiontrabajosgrado.domain.rta_examen_valoracion.RespuestaExamenValoracion;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.rta_examen_valoracion.RespuestaExamenValoracion.RespuestaExamenValoracionBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado;
import com.unicauca.maestria.api.gestiontrabajosgrado.domain.trabajo_grado.TrabajoGrado.TrabajoGradoBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.inicio_trabajo_grado.TrabajoGradoResponseDto.TrabajoGradoResponseDtoBuilder;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto;
import com.unicauca.maestria.api.gestiontrabajosgrado.dtos.respuesta_examen_valoracion.RespuestaExamenValoracionDto.RespuestaExamenValoracionDtoBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-17T22:19:04-0500",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240325-1403, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class RespuestaExamenValoracionMapperImpl implements RespuestaExamenValoracionMapper {

    @Override
    public RespuestaExamenValoracionDto toDto(RespuestaExamenValoracion entity) {
        if ( entity == null ) {
            return null;
        }

        RespuestaExamenValoracionDtoBuilder respuestaExamenValoracionDto = RespuestaExamenValoracionDto.builder();

        respuestaExamenValoracionDto.estadoFinalizado( entity.getEstadoFinalizado() );
        respuestaExamenValoracionDto.fechaMaximaEntrega( entity.getFechaMaximaEntrega() );
        respuestaExamenValoracionDto.idRtaExamenValoracion( entity.getIdRtaExamenValoracion() );
        respuestaExamenValoracionDto.linkFormatoB( entity.getLinkFormatoB() );
        respuestaExamenValoracionDto.linkFormatoC( entity.getLinkFormatoC() );
        respuestaExamenValoracionDto.linkObservaciones( entity.getLinkObservaciones() );
        respuestaExamenValoracionDto.observacion( entity.getObservacion() );
        respuestaExamenValoracionDto.respuestaExamenValoracion( entity.getRespuestaExamenValoracion() );
        respuestaExamenValoracionDto.trabajoGrado( trabajoGradoToTrabajoGradoResponseDto( entity.getTrabajoGrado() ) );

        return respuestaExamenValoracionDto.build();
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

    @Override
    public RespuestaExamenValoracion toEntity(RespuestaExamenValoracionDto dto) {
        if ( dto == null ) {
            return null;
        }

        RespuestaExamenValoracionBuilder respuestaExamenValoracion = RespuestaExamenValoracion.builder();

        respuestaExamenValoracion.estadoFinalizado( dto.getEstadoFinalizado() );
        respuestaExamenValoracion.fechaMaximaEntrega( dto.getFechaMaximaEntrega() );
        respuestaExamenValoracion.idRtaExamenValoracion( dto.getIdRtaExamenValoracion() );
        respuestaExamenValoracion.linkFormatoB( dto.getLinkFormatoB() );
        respuestaExamenValoracion.linkFormatoC( dto.getLinkFormatoC() );
        respuestaExamenValoracion.linkObservaciones( dto.getLinkObservaciones() );
        respuestaExamenValoracion.observacion( dto.getObservacion() );
        respuestaExamenValoracion.respuestaExamenValoracion( dto.getRespuestaExamenValoracion() );
        respuestaExamenValoracion.trabajoGrado( trabajoGradoResponseDtoToTrabajoGrado( dto.getTrabajoGrado() ) );

        return respuestaExamenValoracion.build();
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

    protected TrabajoGradoResponseDto trabajoGradoToTrabajoGradoResponseDto(TrabajoGrado trabajoGrado) {
        if ( trabajoGrado == null ) {
            return null;
        }

        TrabajoGradoResponseDtoBuilder trabajoGradoResponseDto = TrabajoGradoResponseDto.builder();

        trabajoGradoResponseDto.fechaCreacion( trabajoGrado.getFechaCreacion() );
        trabajoGradoResponseDto.id( trabajoGrado.getId() );
        trabajoGradoResponseDto.numeroEstado( trabajoGrado.getNumeroEstado() );
        trabajoGradoResponseDto.titulo( trabajoGrado.getTitulo() );

        return trabajoGradoResponseDto.build();
    }

    protected TrabajoGrado trabajoGradoResponseDtoToTrabajoGrado(TrabajoGradoResponseDto trabajoGradoResponseDto) {
        if ( trabajoGradoResponseDto == null ) {
            return null;
        }

        TrabajoGradoBuilder trabajoGrado = TrabajoGrado.builder();

        trabajoGrado.fechaCreacion( trabajoGradoResponseDto.getFechaCreacion() );
        trabajoGrado.id( trabajoGradoResponseDto.getId() );
        trabajoGrado.numeroEstado( trabajoGradoResponseDto.getNumeroEstado() );
        trabajoGrado.titulo( trabajoGradoResponseDto.getTitulo() );

        return trabajoGrado.build();
    }
}
